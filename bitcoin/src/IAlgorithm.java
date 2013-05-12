import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents an algorithm used for working out whether to buy or sell.
 */
public interface IAlgorithm {

    /**
     * Prime the algorithm with initial values.
     */
    void setInitialTransaction(Ticker transaction);

    /**
     * Optionally specify a debug output file.
     */
    void setDebugFile(FileWriter writer);

    /**
     * Adds the specified transaction to the algorithm's processing. Every
     * transaction that occurs must be added.
     *
     * @return What sort of order should be placed following this transaction.
     * @throws IOException
     *         When writing to the debug file fails.
     */
	OrderType addTransaction(Ticker ticker) throws IOException;
}
