package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class TickerPrice extends MtGoxPrice implements CurrencyKludge {

    private CurrencyInfo currencyInfo;

    public TickerPrice(@JsonProperty("value") double value,
            @JsonProperty("value_int") long value_int,
            @JsonProperty("display") String display,
            @JsonProperty("display_short") String display_short,
            @JsonProperty("currency") String currency) {
        super(value, value_int, display, display_short, currency);
    }

    /*
     * This is a bit of a kludge that ensures the offers know what currency they
     * are in.
     */
    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    /**
     * This function must be called *after* you have setCurrencyInfo on this
     * object.
     *
     * @return the value_int
     */
    public MtGoxFiatCurrency getPriceValue() {
        return new MtGoxFiatCurrency(this.getPriceValueInt(), currencyInfo);
    }
}
