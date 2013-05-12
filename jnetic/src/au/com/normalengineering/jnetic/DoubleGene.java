package au.com.normalengineering.jnetic;

public class DoubleGene extends Gene implements IDoubleGene {
    private static final long serialVersionUID = -2569018978861457077L;

    public static DoubleGene getNewInstance(double min, double max) {
        return new DoubleGene(min, max, -1.0);
    }

    public static DoubleGene getNewInstance(double min, double max, double sd) {
        return new DoubleGene(min, max, sd);
    }

    public static DoubleGene getNewValueInstance(double value) {
        return new DoubleGene(value, value, value, -1.0);
    }

    private DoubleGene(double min, double max, double sd) {
        super(1);
        min_ = min;
        max_ = max;
        sd_ = sd;
        value_ = getRandomValue(min, max);
    }

    private DoubleGene(double value, double min, double max, double sd) {
        super(1);
        min_ = min;
        max_ = max;
        sd_ = sd;
        value_ = value;
    }

    protected DoubleGene(DoubleGene gene, double value) {
        super(1);
        min_ = gene.min_;
        max_ = gene.max_;
        sd_ = gene.sd_;
        value_ = value;
    }

    private static double getRandomValue(double min, double max) {
        return Random.getInstance().nextDouble() * (max - min) + min;
    }

    private static double getRandomValue(double min, double max, double oldValue, double sd) {
        double value = Random.getInstance().nextGaussian() * sd + oldValue;
        value = Math.min(max, value);
        value = Math.max(min, value);
        return value;
    }

    @Override
    DoubleGene getCrossedOverInstance(Gene other, int firstBase, int numBases) {
        return (DoubleGene) other;
    }

    @Override
    DoubleGene getMutatedInstance(int base) {
        if (sd_ <= 0) {
            return new DoubleGene(getRandomValue(min_, max_), min_, max_, sd_);
        } else {
            return new DoubleGene(getRandomValue(min_, max_, value_, sd_), min_, max_, sd_);
        }
    }

    @Override
    public double getValue() {
        return value_;
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", min=" + min_ + ", max=" + max_ + "}";
    }

    private final double min_;
    private final double max_;
    private final double sd_;
    private final double value_;
}
