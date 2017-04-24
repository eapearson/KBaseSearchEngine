package kbaserelationengine.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.http.HttpHost;

import kbaserelationengine.common.GUID;
import kbaserelationengine.events.MongoDBStatusEventStorage;
import kbaserelationengine.events.ObjectStatusEvent;
import kbaserelationengine.events.StatusEventListener;
import kbaserelationengine.events.StatusEventStorage;
import kbaserelationengine.events.WSStatusEventReconstructor;
import kbaserelationengine.events.WSStatusEventTrigger;
import kbaserelationengine.parse.ObjectParseException;
import kbaserelationengine.parse.ObjectParser;
import kbaserelationengine.queue.ObjectStatusEventIterator;
import kbaserelationengine.queue.ObjectStatusEventQueue;
import kbaserelationengine.relations.DefaultRelationStorage;
import kbaserelationengine.relations.RelationStorage;
import kbaserelationengine.search.ElasticIndexingStorage;
import kbaserelationengine.search.IndexingStorage;
import kbaserelationengine.system.DefaultSystemStorage;
import kbaserelationengine.system.ObjectTypeParsingRules;
import kbaserelationengine.system.SystemStorage;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonClientException;
import workspace.ObjectData;
import workspace.SetPermissionsParams;
import workspace.WorkspaceClient;

public class MainObjectProcessor {
    private URL wsURL;
    private AuthToken kbaseIndexerToken;
    private File tempDir;
    private WSStatusEventReconstructor wsEventReconstructor;
    private StatusEventStorage eventStorage;
    private ObjectStatusEventQueue queue;
    private Thread mainRunner;
    private SystemStorage systemStorage;
    private IndexingStorage indexingStorage;
    private RelationStorage relationStorage;
    
    public MainObjectProcessor(URL wsURL, AuthToken kbaseIndexerToken, String mongoHost, 
            int mongoPort, String mongoDbName, HttpHost esHost, String esIndexPrefix, 
            File typesDir, File tempDir, boolean startLifecycleRunner) 
                    throws IOException, ObjectParseException {
        this.wsURL = wsURL;
        this.kbaseIndexerToken = kbaseIndexerToken;
        this.tempDir = tempDir;
        eventStorage = new MongoDBStatusEventStorage(mongoHost, mongoPort, mongoDbName);
        WSStatusEventTrigger eventTrigger = new WSStatusEventTrigger();
        wsEventReconstructor = new WSStatusEventReconstructor(wsURL, kbaseIndexerToken, 
                eventStorage, eventTrigger);
        eventTrigger.registerListener((StatusEventListener)eventStorage);
        queue = new ObjectStatusEventQueue(eventStorage);
        systemStorage = new DefaultSystemStorage(wsURL, typesDir);
        ElasticIndexingStorage esStorage = new ElasticIndexingStorage(esHost);
        esStorage.setIndexNamePrefix(esIndexPrefix);
        indexingStorage = esStorage;
        relationStorage = new DefaultRelationStorage();
        // We switch this flag off in tests 
        if (startLifecycleRunner) {
            startLifecycleRunner();
        }
    }
    
    public void startLifecycleRunner() {
        if (mainRunner != null) {
            throw new IllegalStateException("Lifecycle runner was already started");
        }
        mainRunner = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            performOneTick();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (Exception e) {
                            String codePlace = e.getStackTrace().length > 0 ? 
                                    e.getStackTrace()[0].toString() : "<not-available>";
                                    System.out.println("Error in Lifecycle runner: " + e + ", " + codePlace);
                        }
                    }
                } finally {
                    mainRunner = null;
                }
            }
        });
        mainRunner.start();
    }
    
    public boolean stopLifecycleRunner() {
        if (mainRunner == null) {
            return false;
        }
        if (mainRunner.isInterrupted()) {
            throw new IllegalStateException("Lifecycle Runner can not be stopped twice");
        }
        mainRunner.interrupt();
        // Let's check every 100 ms during 1 minute at most.
        for (int i = 0; i < 600; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            if (mainRunner == null) {
                return true;
            }
        }
        throw new IllegalStateException("Failed to stop Lifecycle Runner");
    }
    
    public void performOneTick() 
            throws IOException, JsonClientException, ObjectParseException {
        wsEventReconstructor.doPublicWorkspaces();
        wsEventReconstructor.doPrivateWorkspaces();
        ObjectStatusEventIterator iter = queue.iterator("WS");
        while (iter.hasNext()) {
            ObjectStatusEvent ev = iter.next();
            if (!isStorageTypeSupported(ev.getStorageObjectType())) {
                System.out.println("Skipping " + ev.getEventType() + ", " + 
                        ev.getStorageObjectType() + ", " + ev.toGUID());
                iter.markAsVisitied(false);
                continue;
            }
            System.out.println("Processing " + ev.getEventType() + ", " + 
                    ev.getStorageObjectType() + ", " + ev.toGUID() + "...");
            long time = System.currentTimeMillis();
            switch (ev.getEventType()) {
            case CREATED:
            case NEW_VERSION:
                indexObject(ev.toGUID(), ev.getStorageObjectType());
                break;
            case DELETED:
                unshare(ev.toGUID(), ev.getAccessGroupId());
                break;
            case SHARED:
                share(ev.toGUID(), ev.getTargetAccessGroupId());
                break;
            case UNSHARED:
                unshare(ev.toGUID(), ev.getTargetAccessGroupId());
                break;
            default:
                throw new IllegalStateException("Unsupported event type: " + ev.getEventType());
            }
            System.out.println("    (processing time: " + (System.currentTimeMillis() - time) + "ms.)");
        }
    }
    
    public boolean isStorageTypeSupported(String storageObjectType) throws IOException {
        return systemStorage.listObjectTypesByStorageObjectType(storageObjectType) != null;
    }
    
    public void indexObject(GUID guid, String storageObjectType) 
            throws IOException, JsonClientException, ObjectParseException {
        File tempFile = ObjectParser.prepareTempFile(tempDir);
        String objRef = guid.getAccessGroupId() + "/" + guid.getAccessGroupObjectId() + "/" +
                guid.getVersion();
        ObjectData obj = ObjectParser.loadObject(wsURL, tempFile, kbaseIndexerToken, objRef);
        List<ObjectTypeParsingRules> parsingRules = 
                systemStorage.listObjectTypesByStorageObjectType(storageObjectType);
        for (ObjectTypeParsingRules rule : parsingRules) {
            ObjectParser.processSubObjects(obj, objRef, rule, systemStorage, 
                    indexingStorage, relationStorage);
        }
    }
    
    public void share(GUID guid, int accessGroupId) throws IOException {
        indexingStorage.shareObject(new LinkedHashSet<>(Arrays.asList(guid)), accessGroupId);
    }

    public void unshare(GUID guid, int accessGroupId) throws IOException {
        indexingStorage.unshareObject(new LinkedHashSet<>(Arrays.asList(guid)), accessGroupId);
    }
    
    public void addWorkspaceToIndex(String wsNameOrId, AuthToken user)
            throws IOException, JsonClientException {
        WorkspaceClient wsClient = new WorkspaceClient(wsURL, user);
        wsClient.setIsInsecureHttpConnectionAllowed(true); 
        SetPermissionsParams params = new SetPermissionsParams();
        try {
            params.setId(Long.parseLong(wsNameOrId));
        } catch (NumberFormatException e) {
            params.setWorkspace(wsNameOrId);
        }
        wsClient.setPermissions(params.withUsers(
                Arrays.asList(kbaseIndexerToken.getUserName())).withNewPermission("w"));
    }
}
