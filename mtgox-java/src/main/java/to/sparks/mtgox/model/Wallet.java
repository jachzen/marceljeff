package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
public class Wallet extends DtoBase implements CurrencyKludge {

    private CurrencyInfo currencyInfo;
    private MtGoxPrice balance;
    private long operations;
    private MtGoxPrice dailyWithdrawLimit;
    private MtGoxPrice monthlyWithdrawLimit;
    private MtGoxPrice maxWithdraw;
    private MtGoxPrice openOrders;

    public Wallet(@JsonProperty("Balance") MtGoxPrice balance,
            @JsonProperty("Operations") long operations,
            @JsonProperty("Daily_Withdraw_Limit") MtGoxPrice dailyWithdrawLimit,
            @JsonProperty("Monthly_Withdraw_Limit") MtGoxPrice monthlyWithdrawLimit,
            @JsonProperty("Max_Withdraw") MtGoxPrice maxWithdraw,
            @JsonProperty("Open_Orders") MtGoxPrice openOrders) {
        this.balance = balance;
        this.operations = operations;
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.monthlyWithdrawLimit = monthlyWithdrawLimit;
        this.maxWithdraw = maxWithdraw;
        this.openOrders = openOrders;
    }

    /**
     * @return the currency
     */
    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    /*
     * Cast the result to either MtGoxBitcoin or MtGoxFiatCurrency depending on
     * which is correct for the situation.
     */
    public MtGoxUnitOfCredit getBalance() {
        MtGoxUnitOfCredit result = new MtGoxBitcoin(balance.getPriceValueInt());
        if (currencyInfo != null && !currencyInfo.isVirtual()) {
            result = new MtGoxFiatCurrency(balance.getPriceValueInt(), currencyInfo);
        }
        return result;
    }

    public long getOperations() {
        return operations;
    }

    public MtGoxPrice getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    public MtGoxPrice getMonthlyWithdrawLimit() {
        return monthlyWithdrawLimit;
    }

    public MtGoxPrice getMaxWithdraw() {
        return maxWithdraw;
    }

    public MtGoxPrice getOpenOrders() {
        return openOrders;
    }
}
