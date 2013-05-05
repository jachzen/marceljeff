package to.sparks.mtgox.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * This bean can be used as a dynamic proxy for *any* JSON object
 * 
 * @author SparksG
 */
public class DynaBean extends DtoBase {

    protected Map<String, Object> other = new HashMap<String, Object>();

    @JsonCreator
    public DynaBean() {
    }

    public Object get(String name) {
        return other.get(name);
    }

    // "any getter" needed for serialization
    @JsonAnyGetter
    public Map<String, Object> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        other.put(name, value);
    }
}