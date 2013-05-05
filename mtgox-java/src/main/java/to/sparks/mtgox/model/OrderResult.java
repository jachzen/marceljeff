package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class OrderResult extends DtoBase implements CurrencyKludge {

    private String order_id;
    private TradeResult[] trades;
    private TickerPrice total_amount;
    private TickerPrice total_spent;
    private TickerPrice avg_cost;

    public OrderResult(@JsonProperty("order_id") String order_id,
            @JsonProperty("trades") TradeResult[] trades,
            @JsonProperty("total_amount") TickerPrice total_amount,
            @JsonProperty("total_spent") TickerPrice total_spent,
            @JsonProperty("avg_cost") TickerPrice avg_cost) {

        this.order_id = order_id;
        this.trades = trades;
        this.total_amount = total_amount;
        this.total_spent = total_spent;
        this.avg_cost = avg_cost;

        if (this.total_amount != null) {
            this.total_amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        for (TradeResult tr : trades) {
            tr.setCurrencyInfo(currencyInfo);
        }
        total_spent.setCurrencyInfo(currencyInfo);
        avg_cost.setCurrencyInfo(currencyInfo);
    }

    public String getOrderId() {
        return order_id;
    }

    public TradeResult[] getTrades() {
        return trades;
    }

    public TickerPrice getTotalAmount() {
        return total_amount;
    }

    public TickerPrice getTotalSpent() {
        return total_spent;
    }

    public TickerPrice getAvgCost() {
        return avg_cost;
    }
}
