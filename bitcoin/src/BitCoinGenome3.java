import java.io.IOException;
import java.util.ArrayList;

import au.com.normalengineering.jnetic.DoubleGene;
import au.com.normalengineering.jnetic.Gene;
import au.com.normalengineering.jnetic.IDoubleGene;

public class BitCoinGenome3 extends BitCoinGenome {
    private static final long serialVersionUID = -5598211287809838902L;

    /**
     * Constructs a new genome with random gene values.
     */
    public BitCoinGenome3() {
        genes_ = new ArrayList<Gene>();

        /* x, where 10^x = half-life TEMA in minutes. */
        genes_.add(DoubleGene.getNewInstance(2.5, 3.5, 0.2)); // [316,3160]

        /* x, where 10^x = stop-loss/buy proportion of price. */
        genes_.add(DoubleGene.getNewInstance(-2.5, -1.5, 0.2)); // [0.00316,0.0316]
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
    public BitCoinGenome3(BitCoinGenome3 other, ArrayList<Gene> genes) {
        genes_ = genes;
    }

    /**
     * Required constructor for deserialization.
     *
     * @param genes
     *        Genes for the newly constructed genome.
     */
    public BitCoinGenome3(ArrayList<Gene> genes) {
        genes_ = genes;
    }

    public double getHlTema() {
        if (hlTema_ < 0) {
            double x = ((IDoubleGene) genes_.get(0)).getValue();
            hlTema_ = Math.pow(10, x);
        }
        return hlTema_; // minutes
    }

    public double getStop() {
        if (stop_ < 0) {
            double x = ((IDoubleGene) genes_.get(1)).getValue();
            stop_ = Math.pow(10, x);
        }
        return stop_;
    }

    @Override
    public void resetProcessing(Ticker ticker) {
        ticker_ = ticker;
        s1HlTema_ = ticker.getPrice();
        s2HlTema_ = ticker.getPrice();
        s3HlTema_ = ticker.getPrice();
        maxPrice_ = ticker.getPrice();
        minPrice_ = ticker.getPrice();
        orderType_ = OrderType.DO_NOTHING;
    }

    @Override
    public OrderType addTicker(Ticker ticker, ArrayList<Double> debugData) throws IOException {

        /* For setting a breakpoint for debugging. */
        // if (new
        // SimpleDateFormat("dd.MM.yyyy").format(ticker.getDate()).equals("24.04.2013"))
        // {
        // ticker = ticker;
        // }

        /* Calculate time since last ticker. */
        double time = ticker.getDate().getTime() / (1000.0 * 60.0) - ticker_.getDate().getTime() / (1000.0 * 60.0); // minutes

        /* Calculate the new moving average prices. TEMA = triple exponential
         * averager. Normal formula is TEMA = 3*S1 - 3*S2 + S3. */
        s1HlTema_ = Algorithms.nextMovingAverageScaled(s1HlTema_, ticker.getPrice(), getHlTema(), time);
        s2HlTema_ = Algorithms.nextMovingAverageScaled(s2HlTema_, s1HlTema_, getHlTema(), time);
        s3HlTema_ = Algorithms.nextMovingAverageScaled(s3HlTema_, s2HlTema_, getHlTema(), time);
        double tema = 3 * s1HlTema_ - 3 * s2HlTema_ + s3HlTema_;

        /* Track max/min prices. */
        maxPrice_ = Math.max(maxPrice_, tema);
        minPrice_ = Math.min(minPrice_, tema);

        /* Stop-loss / stop-buy. */
        double stopLoss = maxPrice_ * (1 - getStop());
        double stopBuy = minPrice_ * (1 + getStop());
        if (orderType_ == OrderType.BUY) {
            if (tema < stopLoss) {
                orderType_ = OrderType.SELL;
                minPrice_ = tema;
            }
        } else {
            if (tema > stopBuy) {
                orderType_ = OrderType.BUY;
                maxPrice_ = tema;
            }
        }

        /* If requested, return debugging info (eg for outputting to a chart). */
        if (debugData != null) {
            debugData.add(tema);
        }

        /* Store values for next call. */
        ticker_ = ticker;

        return orderType_;
    }

    @Override
    public String toString() {
        return String.format("{tema=%.0f stop=%.4f}", getHlTema(), getStop());
    }

    /* Used to increase efficiency by calculating only once. */
    double hlTema_ = -1;
    double stop_ = -1;

    /* Used to track state as algorithm is used. */
    Ticker ticker_ = null;
    double s1HlTema_ = 0;
    double s2HlTema_ = 0;
    double s3HlTema_ = 0;
    double maxPrice_ = 0;
    double minPrice_ = 0;
    OrderType orderType_;
}
