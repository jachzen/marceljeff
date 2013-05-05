package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class TradeResult extends DtoBase implements CurrencyKludge {

    private String trade_id;
    private String primary;
    private String type;
    private String properties;
    private String item;
    private TickerPrice amount;
    private TickerPrice price;
    private TickerPrice spent;
    private String date;

    public TradeResult(@JsonProperty("trade_id") String trade_id,
            @JsonProperty("primary") String primary,
            @JsonProperty("currency") String currency,
            @JsonProperty("type") String type,
            @JsonProperty("properties") String properties,
            @JsonProperty("item") String item,
            @JsonProperty("amount") TickerPrice amount,
            @JsonProperty("price") TickerPrice price,
            @JsonProperty("spent") TickerPrice spent,
            @JsonProperty("date") String date) {
        this.trade_id = trade_id;
        this.primary = primary;
        this.type = type;
        this.properties = properties;
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.spent = spent;
        this.date = date;

        if (this.amount != null) {
            this.amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        price.setCurrencyInfo(currencyInfo);
        spent.setCurrencyInfo(currencyInfo);
    }

    public String getTradeId() {
        return trade_id;
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

    public TickerPrice getAmount() {
        return amount;
    }

    public TickerPrice getPrice() {
        return price;
    }

    public TickerPrice getSpent() {
        return spent;
    }

    public String getDate() {
        return date;
    }
}
