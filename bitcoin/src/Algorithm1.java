import java.io.FileWriter;
import java.io.IOException;

/**
 * This first algorithm is just for software testing purposes.
 */
public class Algorithm1 implements IAlgorithm {

    @Override
    public void setInitialTransaction(Ticker transaction) {
        previousTransaction_ = transaction;
        previousAveragePrice_ = transaction.getPrice();
        orderType_ = OrderType.DO_NOTHING;
    }

    @Override
    public void setDebugFile(FileWriter writer) {
        writer_ = writer;
    }

    @Override
	public OrderType addTransaction(Ticker transaction) throws IOException {
        /* Calculate time since last transaction and the new moving average
         * price. */
        double time = transaction.getDate().getTime() / (1000.0 * 60.0) - previousTransaction_.getDate().getTime()
            / (1000.0 * 60.0);
        double averagePrice = Algorithms.nextMovingAverageScaled(previousAveragePrice_, transaction.getPrice(),
            HALF_LIFE, time);

        /* If price is changing fast enough, change buy or sell state. (This
         * looks for a Scheitelpunkt.) Gradient is the proportion change per
         * minute. */
        double gradient = (averagePrice - previousAveragePrice_) / (time * (averagePrice + previousAveragePrice_) / 2);
        if (gradient > 0.00001) {
            orderType_ = OrderType.BUY;
        } else if (gradient < -0.00001) {
            orderType_ = OrderType.SELL;
        }

        /* Output data for playing with in a spreadsheet, if you want this. */
        if (writer_ != null) {
            long days = transaction.getDate().getTime() / 1000 / 60 / 60 / 24;
            long previousDays = previousTransaction_.getDate().getTime() / 1000 / 60 / 60 / 24;
            if (days != previousDays) {
                int x = orderType_ == OrderType.BUY ? 50 : (orderType_ == OrderType.SELL ? 0 : 25);
                writer_.write(String.format("%d,%f,%f,%d\n", days, transaction.getPrice(), averagePrice, x));
            }
        }

        /* Store values for next call. */
        previousAveragePrice_ = averagePrice;
        previousTransaction_ = transaction;

        return orderType_;
    }

    double HALF_LIFE = 24 * 60; // minutes
    double previousAveragePrice_ = 0;
    Ticker previousTransaction_;
    OrderType orderType_;
    FileWriter writer_ = null;
}
