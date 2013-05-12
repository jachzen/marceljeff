package au.com.normalengineering.jnetic;

public class IntegerBitGene extends BitGene implements IIntegerGene {
    private static final long serialVersionUID = -3440235526385473154L;

    public static IntegerBitGene getNewInstance(int min, int max) {
        int numBases = 0;
        for (int range = max - min; range > 0; range >>= 1) {
            numBases++;
        }
        double step = ((double) (max - min)) / ((1 << numBases) - 1);
        return new IntegerBitGene(min, step, numBases);
    }

    public static IntegerBitGene getNewValueInstance(int value) {
        return new IntegerBitGene(0, value, 0, 0);
    }

    private IntegerBitGene(double min, double step, int numBases) {
        super(numBases, Random.getInstance().nextInt(1 << numBases));
        min_ = min;
        step_ = step;
    }

    private IntegerBitGene(int rawValue, double min, double step, int numBases) {
        super(numBases, rawValue);
        min_ = min;
        step_ = step;
    }

    protected IntegerBitGene(IntegerBitGene gene, int rawValue) {
        super(gene.numBases_, rawValue);
        min_ = gene.min_;
        step_ = gene.step_;
    }

    @Override
    public int getValue() {
        return (int) Math.round(min_ + step_ * rawValue_);
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", min=" + Math.round(min_) + ", max="
            + Math.round((min_ + step_ * ((1 << numBases_) - 1))) + ", numBases=" + numBases_ + ", rawValue="
            + rawValue_ + "}";
    }

    private final double min_;
    private final double step_;
}
