/**
 * Represents an algorithm used for working out whether to buy or sell.
 */
public interface IAlgorithm {

    /**
     * Prime the algorithm with initial values.
     */
    void setInitialTransaction(Transaction transaction);

    /**
     * Adds the specified transaction to the algorithm's processing. Every
     * transaction that occurs must be added.
     *
     * @return What sort of order should be placed following this transaction.
     */
    OrderType addTransaction(Transaction transaction);
}
