package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Offer extends DtoBase implements CurrencyKludge {

    protected CurrencyInfo currencyInfo;
    private long price_int;
    private long stamp;
    private long amount_int;

    public Offer(@JsonProperty("price") double price,
            @JsonProperty("amount") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("stamp") long stamp) {
        this.price_int = price_int;
        this.amount_int = amount_int;
        this.stamp = stamp;
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public MtGoxFiatCurrency getPrice() {
        MtGoxFiatCurrency price = null;
        if (currencyInfo != null) {
            price = new MtGoxFiatCurrency(price_int, currencyInfo);
        } else {
            throw new RuntimeException("Error: getPrice called before currency was initialised.");
        }
        return price;
    }

    public MtGoxBitcoin getAmount() {
        MtGoxBitcoin amount = new MtGoxBitcoin(amount_int);
        return amount;
    }

    public long getStamp() {
        return stamp;
    }

    public void setAmount(MtGoxUnitOfCredit amount) {
        this.amount_int = amount.longValueExact();
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }
}
