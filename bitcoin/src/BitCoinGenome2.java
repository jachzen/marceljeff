import java.io.IOException;
import java.util.ArrayList;

import au.com.normalengineering.jnetic.Gene;

public class BitCoinGenome2 extends BitCoinGenome {
    private static final long serialVersionUID = -5598211287809838902L;

    /**
     * Constructs a new genome with random gene values.
     */
    public BitCoinGenome2() {
        genes_ = new ArrayList<Gene>();
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
    public BitCoinGenome2(BitCoinGenome2 other, ArrayList<Gene> genes) {
        genes_ = genes;
    }

    /**
     * Required constructor for deserialization.
     *
     * @param genes
     *        Genes for the newly constructed genome.
     */
    public BitCoinGenome2(ArrayList<Gene> genes) {
        genes_ = genes;
    }

    @Override
    public void resetProcessing(Ticker ticker) {
        ticker_ = ticker;
        orderType_ = OrderType.DO_NOTHING;
    }

    @Override
    public OrderType addTicker(Ticker ticker, ArrayList<Double> debugData) throws IOException {

        /* Calculate time since last ticker. */
        double time = ticker.getDate().getTime() / (1000.0 * 60.0); // minutes

        /* Transaction every 10 minutes. */
        final double repeatTime = 10;
        if (((long) time) % ((long) repeatTime) == 0) {
            if (orderType_ == OrderType.BUY) {
                orderType_ = OrderType.SELL;
            } else {
                orderType_ = OrderType.BUY;
            }
        }

        /* Store values for next call. */
        ticker_ = ticker;

        return orderType_;
    }

    @Override
    public String toString() {
        return String.format("{repeated buy/sell demo}");
    }

    /* Used to track state as algorithm is used. */
    Ticker ticker_ = null;
    OrderType orderType_;
}
