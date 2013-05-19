import java.io.IOException;
import java.util.ArrayList;

import au.com.normalengineering.jnetic.DoubleGene;
import au.com.normalengineering.jnetic.Gene;
import au.com.normalengineering.jnetic.IDoubleGene;

public class BitCoinGenome1 extends BitCoinGenome {
    private static final long serialVersionUID = -5598211287809838902L;

    /**
     * Constructs a new genome with random gene values.
     */
    public BitCoinGenome1() {
        genes_ = new ArrayList<Gene>();

        /* x, where 10^x = half-life in minutes */
        genes_.add(DoubleGene.getNewInstance(0, 4, 2));

        /* x, where 10^x = switchGradient. */
        genes_.add(DoubleGene.getNewInstance(-6, -2, 2));
    }

    /**
     * Required constructor which copies the derived-type specific fields from
     * another genome of this same derived type.
     *
     * @param other
     *        Other genome to copy fields from.
     * @param genes
     *        Genes for the newly constructed genome.
     */
    public BitCoinGenome1(BitCoinGenome1 other, ArrayList<Gene> genes) {
        genes_ = genes;
    }

    /**
     * Required constructor for deserialization.
     *
     * @param genes
     *        Genes for the newly constructed genome.
     */
    public BitCoinGenome1(ArrayList<Gene> genes) {
        genes_ = genes;
    }

    public double getHalfLife() {
        if (halfLife_ < 0) {
            double x = ((IDoubleGene) genes_.get(0)).getValue();
            halfLife_ = Math.pow(10, x);
        }
        return halfLife_; // minutes
    }

    public double getSwitchGradient() {
        if (switchGradient_ < 0) {
            double x = ((IDoubleGene) genes_.get(1)).getValue();
            switchGradient_ = Math.pow(10, x);
        }
        return switchGradient_;
    }

    @Override
    public void resetProcessing(Ticker ticker) {
        previousTicker_ = ticker;
        previousAveragePrice_ = ticker.getPrice();
        orderType_ = OrderType.DO_NOTHING;
    }

    @Override
    public OrderType addTicker(Ticker ticker) throws IOException {

        /* For setting a breakpoint for debugging. */
        // if (new
        // SimpleDateFormat("dd.MM.yyyy").format(ticker.getDate()).equals("24.04.2013")
        // ) {
        // ticker = ticker;
        // }

        /* Calculate time since last ticker and the new moving average price. */
        double time = ticker.getDate().getTime() / (1000.0 * 60.0) - previousTicker_.getDate().getTime()
            / (1000.0 * 60.0); // minutes
        double averagePrice = Algorithms.nextMovingAverageScaled(previousAveragePrice_, ticker.getPrice(),
            getHalfLife(), time);

        /* If price is changing fast enough, change buy or sell state. (This
         * looks for a Scheitelpunkt.) Gradient is the proportion change per
         * minute. */
        double gradient = (averagePrice - previousAveragePrice_) / (time * (averagePrice + previousAveragePrice_) / 2);
        if (gradient > getSwitchGradient()) {
            orderType_ = OrderType.BUY;
        } else if (gradient < -getSwitchGradient()) {
            orderType_ = OrderType.SELL;
        }

        /* Store values for next call. */
        previousAveragePrice_ = averagePrice;
        previousTicker_ = ticker;

        return orderType_;
    }

    @Override
    public String toString() {
        return String.format("{halfLife=%.1f switchGradient=%f}", getHalfLife(), getSwitchGradient());
    }

    /* Used to increase efficiency by calculating only once. */
    double halfLife_ = -1;
    double switchGradient_ = -1;

    /* Used to track state as algorithm is used. */
    Ticker previousTicker_ = null;
    double previousAveragePrice_ = 0;
    OrderType orderType_;
}
