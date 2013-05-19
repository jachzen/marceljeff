import org.apache.log4j.Logger;

/**
 * Analysis of mtgox: - marketorders (buy/sell at marketprice) execute
 * immediately, therefore we do a very simple simulator in the first step -
 * needs more analysis to make other orders (perhaps add to
 * ordertype(marketorder/normalorder))
 *
 * @author mpater
 */
public class ExchangeSimulator implements IExchange {

    private final Logger logger = Logger.getLogger(ExchangeSimulator.class.getName());

    private int x = 0;
    private final Wallet wallet = new Wallet(IConfiguration.BALANCE);
    private final Database database_;

    /**
     * Constructs an exchange simulator based on the specified database.
     */
    public ExchangeSimulator(Database database) {
        database_ = database;
    }

    @Override
	public Ticker getTicker() {
		Ticker t = database_.getTickers().get(x);
		return t;
    }

    public Ticker getLastTicker() {
        return database_.getTickers().get(database_.getTickers().size() - 1);
    }

    /**
     * Increments to the next ticker.
     *
     * @return true if there are more tickers in the database; false
     *         otherwise.
     */
    public boolean increment() {
        return ++x < database_.getTickers().size();
    }

    @Override
    public Order placeBuyOrder(NormalOrder order) {
        if (wallet.getBalance() > 0) {
            // immediate ticker
            double currentPrice = database_.getTickers().get(x).getPrice();
            double commissionPrice = currentPrice * (1 + IConfiguration.FEE_PERCENTAGE / 100);
            double numberBitCoinsToBuy = wallet.getBalance() / commissionPrice;
            wallet.setBitCoins(numberBitCoinsToBuy);
            wallet.setBalance(0);
            order.setState(Order.State.DONE);
            logger.debug(String.format("%s BUY  %6.2f BitCoins @ EUR%.2f each (+%.1f%% commission)",
                Ticker.DATE_FORMAT.format(database_.getTickers().get(x).getDate()), numberBitCoinsToBuy,
                currentPrice, IConfiguration.FEE_PERCENTAGE));
        }
        return order;
    }

    @Override
    public Order placeSellOrder(NormalOrder order) {
        if (wallet.getBitCoins() > 0) {
            // immediate ticker
            double currentPrice = database_.getTickers().get(x).getPrice();
            double commissionPrice = currentPrice;
            double numberBitCoinsToSell = wallet.getBitCoins();
            wallet.setBalance(numberBitCoinsToSell * commissionPrice);
            wallet.setBitCoins(0);
            order.setState(Order.State.DONE);
            logger.debug(String.format("%s SELL %6.2f BitCoins @ EUR%.2f each, wallet=EUR%.2f",
                Ticker.DATE_FORMAT.format(database_.getTickers().get(x).getDate()), numberBitCoinsToSell,
                currentPrice, wallet.getBalance()));
        }
        return order;
    }

    @Override
    public Order getCurrentOrder() {
        return new NoOrder();
    }

    @Override
	public boolean cancelOrder(Order order) {
        logger.debug("Cancel order " + order);
		return true;
    }

    @Override
    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public Order placeBuyOrder(MarketOrder order) {
        logger.info("Put buy order " + order);
        // immediate ticker
        wallet.withdraw(getTicker().getPrice() + (IConfiguration.FEE_PERCENTAGE * getTicker().getPrice()));
        order.setState(Order.State.DONE);
        return order;
    }

    @Override
    public Order placeSellOrder(MarketOrder order) {
        logger.info("Put sell order " + order);
        // immediate ticker
        wallet.deposit(getTicker().getPrice());
        order.setState(Order.State.DONE);
        return order;
    }
}
