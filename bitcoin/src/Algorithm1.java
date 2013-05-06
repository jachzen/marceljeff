/**
 * This first algorithm is just for software testing purposes.
 */
public class Algorithm1 implements IAlgorithm {

    @Override
    public OrderType addTransaction(Transaction transaction) {
        /* This is a really simple and no doubt useless moving average, just for
         * initial test purposes. */
        price_ = Algorithms.nextMovingAverageHl(price_, transaction.getPrice(), 20);
        return OrderType.DO_NOTHING;
    }

    double price_ = 0;
}
