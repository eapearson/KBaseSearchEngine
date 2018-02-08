
package kbasesearchengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: PostProcessing</p>
 * <pre>
 * Rules for what to return about found objects.
 * skip_info - do not include brief info for object ('guid,
 *     'parent_guid', 'object_name' and 'timestamp' fields in
 *     ObjectData structure),
 * skip_keys - do not include keyword values for object 
 *     ('key_props' field in ObjectData structure),
 * skip_data - do not include raw data for object ('data' and 
 *     'parent_data' fields in ObjectData structure),
 * skip_highlight - do not include highlights of fields that
 *      matched query,
 * ids_only - shortcut to mark all three skips as true.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ids_only",
    "skip_info",
    "skip_keys",
    "skip_data",
    "skip_highlight",
    "data_includes"
})
public class PostProcessing {

    @JsonProperty("ids_only")
    private Long idsOnly;
    @JsonProperty("skip_info")
    private Long skipInfo;
    @JsonProperty("skip_keys")
    private Long skipKeys;
    @JsonProperty("skip_data")
    private Long skipData;
    @JsonProperty("skip_highlight")
    private Long skipHighlight;
    @JsonProperty("data_includes")
    private List<String> dataIncludes;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("ids_only")
    public Long getIdsOnly() {
        return idsOnly;
    }

    @JsonProperty("ids_only")
    public void setIdsOnly(Long idsOnly) {
        this.idsOnly = idsOnly;
    }

    public PostProcessing withIdsOnly(Long idsOnly) {
        this.idsOnly = idsOnly;
        return this;
    }

    @JsonProperty("skip_info")
    public Long getSkipInfo() {
        return skipInfo;
    }

    @JsonProperty("skip_info")
    public void setSkipInfo(Long skipInfo) {
        this.skipInfo = skipInfo;
    }

    public PostProcessing withSkipInfo(Long skipInfo) {
        this.skipInfo = skipInfo;
        return this;
    }

    @JsonProperty("skip_keys")
    public Long getSkipKeys() {
        return skipKeys;
    }

    @JsonProperty("skip_keys")
    public void setSkipKeys(Long skipKeys) {
        this.skipKeys = skipKeys;
    }

    public PostProcessing withSkipKeys(Long skipKeys) {
        this.skipKeys = skipKeys;
        return this;
    }

    @JsonProperty("skip_data")
    public Long getSkipData() {
        return skipData;
    }

    @JsonProperty("skip_data")
    public void setSkipData(Long skipData) {
        this.skipData = skipData;
    }

    public PostProcessing withSkipData(Long skipData) {
        this.skipData = skipData;
        return this;
    }

    @JsonProperty("skip_highlight")
    public Long getSkipHighlight() {
        return skipHighlight;
    }

    @JsonProperty("skip_highlight")
    public void setSkipHighlight(Long skipHighlight) {
        this.skipHighlight = skipHighlight;
    }

    public PostProcessing withSkipHighlight(Long skipHighlight) {
        this.skipHighlight = skipHighlight;
        return this;
    }

    @JsonProperty("data_includes")
    public List<String> getDataIncludes() {
        return dataIncludes;
    }

    @JsonProperty("data_includes")
    public void setDataIncludes(List<String> dataIncludes) {
        this.dataIncludes = dataIncludes;
    }

    public PostProcessing withDataIncludes(List<String> dataIncludes) {
        this.dataIncludes = dataIncludes;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((((((("PostProcessing"+" [idsOnly=")+ idsOnly)+", skipInfo=")+ skipInfo)+", skipKeys=")+ skipKeys)+", skipData=")+ skipData)+", skipHighlight=")+ skipHighlight)+", dataIncludes=")+ dataIncludes)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
