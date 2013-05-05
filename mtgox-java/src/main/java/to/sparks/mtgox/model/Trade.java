package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Trade extends DtoBase implements IEventTime, CurrencyKludge {

    private long amount_int;
    private long price_int;
    private long date;
    private String item;
    private String type;
    private String primary;
    private String properties;
    private String tid;
    private String trade_type;
    private CurrencyInfo currencyInfo = null;
    private String price_currency;
    private double amount;
	private double price;

    public Trade(@JsonProperty("tid") String tid,
            @JsonProperty("primary") String primary,
            @JsonProperty("price_currency") String price_currency,
            @JsonProperty("type") String type,
            @JsonProperty("properties") String properties,
            @JsonProperty("item") String item,
            @JsonProperty("amount") double amount,
            @JsonProperty("price") double price,
            @JsonProperty("trade_type") String trade_type,
            @JsonProperty("date") long date,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("price_int") long price_int) {
        this.tid = tid;
        this.primary = primary;
        this.type = type;
        this.properties = properties;
        this.item = item;
        this.trade_type = trade_type;
        this.date = date;
        this.amount_int = amount_int;
        this.price_int = price_int;
        this.price_currency=price_currency;        
        this.amount = amount;
        this.price = price;
    }

    public String getTradeId() {
        return tid;
    }

    public String getPrimary() {
        return primary;
    }

    public String getType() {
        return type;
    }

    public String getProperties() {
        return properties;
    }

    public String getItem() {
        return item;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public long getDate() {
        return date;
    }

    public MtGoxBitcoin getAmount() {
        MtGoxBitcoin amount = new MtGoxBitcoin(amount_int);
        return amount;
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

    @Override
    public long getEventTime() {
        return getDate();
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public String getPrice_currency() {
        return price_currency;
    }
    
    /**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}
