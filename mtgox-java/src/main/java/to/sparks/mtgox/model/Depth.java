package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Depth extends Offer implements IEventTime, CurrencyKludge {

    private String item;
    private String type_str;
    private int type;
    private long total_volume_int;

    public Depth(@JsonProperty("currency") String currency,
            @JsonProperty("item") String item,
            @JsonProperty("total_volume_int") long total_volume_int,
            @JsonProperty("type_str") String type_str,
            @JsonProperty("type") int type,
            @JsonProperty("price") double price,
            @JsonProperty("volume") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("volume_int") long amount_int,
            @JsonProperty("now") long stamp) {
        super(price, amount, price_int, amount_int, stamp);
        this.item = item;
        this.total_volume_int = total_volume_int;
        this.type_str = type_str;
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public MtGoxBitcoin getTotalVolume() {
        return new MtGoxBitcoin(total_volume_int);
    }

    public String getType_str() {
        return type_str;
    }

    public int getType() {
        return type;
    }

    @Override
    public long getEventTime() {
        return getStamp();
    }
}
