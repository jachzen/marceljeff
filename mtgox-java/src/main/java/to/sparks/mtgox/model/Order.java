package to.sparks.mtgox.model;

import java.util.Currency;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import to.sparks.mtgox.MtGoxHTTPClient;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Order extends DtoBase implements CurrencyKludge {

    private String oid;
    private String currency;
    private String item;
    private MtGoxHTTPClient.OrderType type;
    private TickerPrice amount;
    private TickerPrice effective_amount;
    private TickerPrice price;
    private String status;
    private String date;
    private String priority;
    private DynaBean[] actions;
    private TickerPrice invalid_amount;

    public Order(@JsonProperty("oid") String oid,
            @JsonProperty("currency") String currency,
            @JsonProperty("item") String item,
            @JsonProperty("type") String type,
            @JsonProperty("amount") TickerPrice amount,
            @JsonProperty("effective_amount") TickerPrice effective_amount,
            @JsonProperty("price") TickerPrice price,
            @JsonProperty("status") String status,
            @JsonProperty("date") String date,
            @JsonProperty("priority") String priority,
            @JsonProperty("actions") DynaBean[] actions,
            @JsonProperty("invalid_amount") TickerPrice invalid_amount) {
        this.oid = oid;
        this.currency = currency;
        this.item = item;
        this.type = type != null && type.equalsIgnoreCase("ask") ? MtGoxHTTPClient.OrderType.Ask : MtGoxHTTPClient.OrderType.Bid;
        this.amount = amount;
        this.effective_amount = effective_amount;
        this.price = price;
        this.status = status;
        this.date = date;
        this.priority = priority;
        this.actions = actions;
        this.invalid_amount = invalid_amount;

        if (this.amount != null) {
            this.amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
        if (this.effective_amount != null) {
            this.effective_amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
        if (this.invalid_amount != null) {
            this.invalid_amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
    }

    public String getOid() {
        return oid;
    }

    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    public String getItem() {
        return item;
    }

    public MtGoxHTTPClient.OrderType getType() {
        return type;
    }

    public TickerPrice getAmount() {
        return amount;
    }

    public TickerPrice getValidAmount() {
        return effective_amount;
    }

    public TickerPrice getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }

    public DynaBean[] getActions() {
        return actions;
    }

    public TickerPrice getInvalidAmount() {
        return invalid_amount;
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        price.setCurrencyInfo(currencyInfo);
    }
}
