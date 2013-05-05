package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SparksG
 */
@JsonAutoDetect
public class FullDepth extends DtoBase implements CurrencyKludge {

    private Offer[] asks;
    private Offer[] bids;

    @JsonCreator
    public FullDepth(@JsonProperty("asks") Offer[] asks,
            @JsonProperty("bids") Offer[] bids) {
        this.asks = asks;
        this.bids = bids;
    }

    /*
     * This is a bit of a kludge that ensures the offers know what currency they
     * are in.
     */
    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        for (Offer ask : asks) {
            ask.setCurrencyInfo(currencyInfo);
        }
        for (Offer bid : bids) {
            bid.setCurrencyInfo(currencyInfo);
        }
    }

    public Offer[] getAsks() {
        return asks;
    }

    public Offer[] getBids() {
        return bids;
    }
}
