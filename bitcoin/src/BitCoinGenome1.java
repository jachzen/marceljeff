import java.io.IOException;
import java.text.SimpleDateFormat;
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

        /* x, where 10^x = half-life DEMA-past in minutes. */
        genes_.add(DoubleGene.getNewInstance(0, 4, 1));

        /* x, where 10^x = half-life DEMA=future in minutes. */
        genes_.add(DoubleGene.getNewInstance(0, 4, 1));

        /* x, where 10^x = half-life TEMA in minutes. */
        genes_.add(DoubleGene.getNewInstance(0, 4, 1));

        /* Proportion of EMA which is DEMA [0,1], the rest is TEMA. */
        genes_.add(DoubleGene.getNewInstance(0, 1, 0.3));

        /* x, where 10^x = stop-loss/buy proportion of price. */
        genes_.add(DoubleGene.getNewInstance(-4, -1, 1));
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

    public double getHlDemaPast() {
        if (hlDemaPast_ < 0) {
            double x = ((IDoubleGene) genes_.get(0)).getValue();
            hlDemaPast_ = Math.pow(10, x);
        }
        return hlDemaPast_; // minutes
    }

    public double getHlDemaFuture() {
        if (hlDemaFuture_ < 0) {
            double x = ((IDoubleGene) genes_.get(1)).getValue();
            hlDemaFuture_ = Math.pow(10, x);
        }
        return hlDemaFuture_; // minutes
    }

    public double getHlTema() {
        if (hlTema_ < 0) {
            double x = ((IDoubleGene) genes_.get(2)).getValue();
            hlTema_ = Math.pow(10, x);
        }
        return hlTema_; // minutes
    }

    public double getDemaProportion() {
        double x = ((IDoubleGene) genes_.get(3)).getValue();
        return x; // [0,1]
    }

    public double getStop() {
        if (stop_ < 0) {
            double x = ((IDoubleGene) genes_.get(4)).getValue();
            stop_ = Math.pow(10, x);
        }
        return stop_;
    }

    @Override
    public void resetProcessing(Ticker ticker) {
        ticker_ = ticker;
        s1HlDemaPast_ = ticker.getPrice();
        s1HlDemaFuture_ = ticker.getPrice();
        s2HlDemaFuture_ = ticker.getPrice();
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

        /* Calculate the new moving average prices. DEMA = double exponential
         * averager. TEMA = triple exponential averager. Normal formulas are
         * DEMA = 2*S1-S2 and TEMA = 3*S1 - 3*S2 + S3. DEMA is split into past
         * and future sections and calculated from the average. EMA is
         * calculated from a mix of DEMA and TEMA. */
        s1HlDemaPast_ = Algorithms.nextMovingAverageScaled(s1HlDemaPast_, ticker.getPrice(), getHlDemaPast(), time);
        s1HlDemaFuture_ = Algorithms.nextMovingAverageScaled(s1HlDemaFuture_, ticker.getPrice(), getHlDemaFuture(),
            time);
        s2HlDemaFuture_ = Algorithms.nextMovingAverageScaled(s2HlDemaFuture_, s1HlDemaFuture_, getHlDemaFuture(), time);
        s1HlTema_ = Algorithms.nextMovingAverageScaled(s1HlTema_, ticker.getPrice(), getHlTema(), time);
        s2HlTema_ = Algorithms.nextMovingAverageScaled(s2HlTema_, s1HlTema_, getHlTema(), time);
        s3HlTema_ = Algorithms.nextMovingAverageScaled(s3HlTema_, s2HlTema_, getHlTema(), time);
        double dema = (s1HlDemaPast_ + 3 * s1HlDemaFuture_ - 2 * s2HlDemaFuture_) / 2;
        double tema = 3 * s1HlTema_ - 3 * s2HlTema_ + s3HlTema_;
        double ema = dema * getDemaProportion() + tema * (1 - getDemaProportion());

        /* Track max/min prices. */
        maxPrice_ = Math.max(maxPrice_, ema);
        minPrice_ = Math.min(minPrice_, ema);

        /* Stop-loss / stop-buy. */
        double stopLoss = maxPrice_ * (1 - getStop());
        double stopBuy = minPrice_ * (1 + getStop());
        if (orderType_ == OrderType.BUY) {
            if (ema < stopLoss) {
                orderType_ = OrderType.SELL;
                minPrice_ = ema;
            }
        } else {
            if (ema > stopBuy) {
                orderType_ = OrderType.BUY;
                maxPrice_ = ema;
            }
        }

        /* If requested, return debugging info (eg for outputting to a chart). */
        if (debugData != null) {
            debugData.add(ema);
        }

        /* Store values for next call. */
        ticker_ = ticker;

        return orderType_;
    }

    @Override
    public String toString() {
        return String.format("{hlDP=%.1f hlDF=%.1f hlT=%.1f prop=%.2f stop=%f}", getHlDemaPast(), getHlDemaFuture(),
            getHlTema(), getDemaProportion(), getStop());
    }

    /* Used to increase efficiency by calculating only once. */
    double hlDemaPast_ = -1;
    double hlDemaFuture_ = -1;
    double hlTema_ = -1;
    double stop_ = -1;

    /* Used to track state as algorithm is used. */
    Ticker ticker_ = null;
    double s1HlDemaPast_ = 0;
    double s1HlDemaFuture_ = 0;
    double s2HlDemaFuture_ = 0;
    double s1HlTema_ = 0;
    double s2HlTema_ = 0;
    double s3HlTema_ = 0;
    double maxPrice_ = 0;
    double minPrice_ = 0;
    OrderType orderType_;
}
