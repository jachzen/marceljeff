package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class OpPrivateTicker extends OpPrivate<Ticker> {

    private Ticker ticker;

    public OpPrivateTicker(@JsonProperty("op") String op,
            @JsonProperty("channel") String channel,
            @JsonProperty("channel_name") String channel_name,
            @JsonProperty("private") String messageType,
            @JsonProperty("ticker") Ticker ticker,
            @JsonProperty("origin") String origin) {
        super(op, channel, channel_name, messageType, origin);
        this.ticker = ticker;
    }

    public Ticker getTicker() {
        return ticker;
    }
}
