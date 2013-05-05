package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
public class MtGoxPrice extends DtoBase {

    private String display;
    private String display_short;
    private long value_int;
    protected String currency;

    public MtGoxPrice(@JsonProperty("value") double value,
            @JsonProperty("value_int") long value_int,
            @JsonProperty("display") String display,
            @JsonProperty("display_short") String display_short,
            @JsonProperty("currency") String currency) {
        this.value_int = value_int;
        this.display = display;
        this.display_short = display_short;
        this.currency = currency;
    }

    public long getPriceValueInt() {
        return value_int;
    }

    public String getDisplay() {
        return display;
    }

    public String getDisplay_short() {
        return display_short;
    }

    public String getCurrencyCode() {
        return currency;
    }
}
