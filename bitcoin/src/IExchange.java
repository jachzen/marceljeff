/**
 * Represents an exchange, which may be for example MtGox, a simulator, or some
 * other exchange. Prices are always in Euro.
 */
public interface IExchange {

    /**
     * Returns the latest price. (Should this be instead getLastTransaction()?)
     */
    double getCurrentPrice();

    /**
     * Places a buy order and returns an object representing the placed order.
     * The order is not carried out immediately; use getCurrentOrder() to find
     * out if the order has been filled.
     */
    Order placeBuyOrder();

    /**
     * Places a sell order and returns an object representing the placed order.
     * The order is not carried out immediately; use getCurrentOrder() to find
     * out if the order has been filled.
     */
    Order placeSellOrder();

    /**
     * Returns the current order, or null if there is no current order (ie the
     * last order has been filled). Currently, only one order at a time is
     * supported.
     */
    Order getCurrentOrder();

    /**
     * Cancel the specified order.
     */
    void cancelOrder(Order order);
}
