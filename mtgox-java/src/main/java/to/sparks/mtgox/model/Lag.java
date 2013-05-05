package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
@JsonIgnoreProperties
public class Lag extends DtoBase {

    private long lag;
    private long lagSecs;
    private String lagText;

	public Lag(@JsonProperty("lag") long lag, @JsonProperty("lag_secs") long lagSecs, @JsonProperty("lag_text") String lagText, @JsonProperty("length") String length) {
        this.lag = lag;
        this.lagSecs = lagSecs;
        this.lagText = lagText;
    }

    public long getLag() {
        return lag;
    }

    public long getLagSecs() {
        return lagSecs;
    }

    public String getLagText() {
        return lagText;
    }
}
