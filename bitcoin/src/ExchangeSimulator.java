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
    private double bitCoins = 0;
    private final Database database_;

    /**
     * Constructs an exchange simulator based on the specified database.
     */
    public ExchangeSimulator(Database database) {
        database_ = database;
    }

    @Override
	public Ticker getTicker() {
		Ticker t = database_.getTransactions().get(x);
		return t;
    }

    /**
     * Increments to the next transaction.
     *
     * @return true if there are more transactions in the database; false
     *         otherwise.
     */
    public boolean increment() {
        return ++x < database_.getTransactions().size();
    }

    @Override
    public Order placeBuyOrder(NormalOrder order) {
        if (wallet.getBalance() > 0) {
            // immediate transaction
            double currentPrice = database_.getTransactions().get(x).getPrice();
            double commissionPrice = currentPrice * (1 + IConfiguration.FEE_PERCENTAGE / 100);
            double numberBitCoinsToBuy = wallet.getBalance() / commissionPrice;
            bitCoins += numberBitCoinsToBuy;
            wallet.setBalance(0);
            order.setState(Order.State.DONE);
            logger.info(String.format("%s BUY  %6.2f BitCoins @ EUR%.2f each (+%.1f%% commission)",
                Ticker.DATE_FORMAT.format(database_.getTransactions().get(x).getDate()), numberBitCoinsToBuy,
                currentPrice, IConfiguration.FEE_PERCENTAGE));
        }
        return order;
    }

    @Override
    public Order placeSellOrder(NormalOrder order) {
        if (bitCoins > 0) {
            // immediate transaction
            double currentPrice = database_.getTransactions().get(x).getPrice();
            double commissionPrice = currentPrice;
            double numberBitCoinsToSell = bitCoins;
            wallet.deposit(numberBitCoinsToSell * commissionPrice);
            bitCoins = 0;
            order.setState(Order.State.DONE);
            logger.info(String.format("%s SELL %6.2f BitCoins @ EUR%.2f each, wallet=EUR%.2f",
                Ticker.DATE_FORMAT.format(database_.getTransactions().get(x).getDate()), numberBitCoinsToSell,
                currentPrice, wallet.getBalance()));
        }
        return order;
    }

    @Override
	public boolean cancelOrder(Order order) {
        logger.debug("Cancell order " + order);
		return true;
    }

    @Override
    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public Order placeBuyOrder(MarketOrder order) {
        logger.info("Put buy order " + order);
        // immediate transaction
		wallet.withdraw(getTicker().getPrice() + (IConfiguration.FEE_PERCENTAGE * getTicker().getPrice()));
        order.setState(Order.State.DONE);
        return order;
    }

    @Override
    public Order placeSellOrder(MarketOrder order) {
        logger.info("Put sell order " + order);
        // immediate transaction
		wallet.deposit(getTicker().getPrice());
        order.setState(Order.State.DONE);
        return order;
    }

	@Override
	public Order getCurrentOrder() {
		// TODO Auto-generated method stub
		return null;
	}
}
