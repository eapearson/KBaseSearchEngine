
package kbasesearchengine;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: KeyDescription</p>
 * <pre>
 * Description of searchable type keyword. 
 *     - key_value_type can be one of {'string', 'integer', 'double', 
 *       'boolean'},
 *     - hidden - if true then this keyword provides values for other
 *       keywords (like in 'link_key') and is not supposed to be shown.
 *     - link_key - optional field pointing to another keyword (which is
 *       often hidden) providing GUID to build external URL to.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "key_name",
    "key_ui_title",
    "key_value_type",
    "hidden",
    "link_key"
})
public class KeyDescription {

    @JsonProperty("key_name")
    private String keyName;
    @JsonProperty("key_ui_title")
    private String keyUiTitle;
    @JsonProperty("key_value_type")
    private String keyValueType;
    @JsonProperty("hidden")
    private Long hidden;
    @JsonProperty("link_key")
    private String linkKey;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("key_name")
    public String getKeyName() {
        return keyName;
    }

    @JsonProperty("key_name")
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public KeyDescription withKeyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    @JsonProperty("key_ui_title")
    public String getKeyUiTitle() {
        return keyUiTitle;
    }

    @JsonProperty("key_ui_title")
    public void setKeyUiTitle(String keyUiTitle) {
        this.keyUiTitle = keyUiTitle;
    }

    public KeyDescription withKeyUiTitle(String keyUiTitle) {
        this.keyUiTitle = keyUiTitle;
        return this;
    }

    @JsonProperty("key_value_type")
    public String getKeyValueType() {
        return keyValueType;
    }

    @JsonProperty("key_value_type")
    public void setKeyValueType(String keyValueType) {
        this.keyValueType = keyValueType;
    }

    public KeyDescription withKeyValueType(String keyValueType) {
        this.keyValueType = keyValueType;
        return this;
    }

    @JsonProperty("hidden")
    public Long getHidden() {
        return hidden;
    }

    @JsonProperty("hidden")
    public void setHidden(Long hidden) {
        this.hidden = hidden;
    }

    public KeyDescription withHidden(Long hidden) {
        this.hidden = hidden;
        return this;
    }

    @JsonProperty("link_key")
    public String getLinkKey() {
        return linkKey;
    }

    @JsonProperty("link_key")
    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public KeyDescription withLinkKey(String linkKey) {
        this.linkKey = linkKey;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ((((((((((((("KeyDescription"+" [keyName=")+ keyName)+", keyUiTitle=")+ keyUiTitle)+", keyValueType=")+ keyValueType)+", hidden=")+ hidden)+", linkKey=")+ linkKey)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
