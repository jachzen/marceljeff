package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public abstract class Operation extends DtoBase {

    private String op;

    public Operation(@JsonProperty("op") String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }
}
