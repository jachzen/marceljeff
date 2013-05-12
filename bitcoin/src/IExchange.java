
/**
 * Represents an exchange, which may be for example MtGox, a simulator, or some
 * other exchange. Prices are always in Euro.
 */
public interface IExchange {

    /**
     * Returns the latest transaction.
     */
	Ticker getTicker();

    /**
     * Places a buy order and returns an object representing the placed order.
     * The order is not carried out immediately; use getCurrentOrder() to find
     * out if the order has been filled.
     */
    Order placeBuyOrder(NormalOrder order);

    /**
     * Places a sell order and returns an object representing the placed order.
     * The order is not carried out immediately; use getCurrentOrder() to find
     * out if the order has been filled.
     */
    Order placeSellOrder(NormalOrder order);

    /**
     * Places a buy order and returns an object representing the placed order.
     * This order is carried out immediately.
     */
    Order placeBuyOrder(MarketOrder order);

    /**
     * Places a sell order and returns an object representing the placed order.
     * This order is carried out immediately.
     */
    Order placeSellOrder(MarketOrder order);

    /**
     * Returns the current order, or null if there is no current order (ie the
     * last order has been filled). Currently, only one order at a time is
     * supported.
     */
	Order getCurrentOrder();

    /**
     * Cancels the specified order, returns NoOrder.
     */
	boolean cancelOrder(Order order);

    /**
     * Get Walletinfo
     */
    Wallet getWallet();

}
