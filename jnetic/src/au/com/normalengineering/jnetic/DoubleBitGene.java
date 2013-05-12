package au.com.normalengineering.jnetic;

public class DoubleBitGene extends BitGene {
    private static final long serialVersionUID = -73566438665916393L;

    public static DoubleBitGene getNewInstance(double min, double max, int numBits) {
        double step = (max - min) / ((1 << numBits) - 1);
        return new DoubleBitGene(min, step, numBits);
    }

    public static DoubleBitGene getNewValueInstance(double value) {
        return new DoubleBitGene(0, value, 0, 0);
    }

    private DoubleBitGene(double min, double step, int numBases) {
        super(numBases, Random.getInstance().nextInt(1 << numBases));
        min_ = min;
        step_ = step;
    }

    private DoubleBitGene(int rawValue, double min, double step, int numBases) {
        super(numBases, rawValue);
        min_ = min;
        step_ = step;
    }

    protected DoubleBitGene(DoubleBitGene gene, int rawValue) {
        super(gene.numBases_, rawValue);
        min_ = gene.min_;
        step_ = gene.step_;
    }

    public double getValue() {
        return min_ + step_ * rawValue_;
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", min=" + min_ + ", max=" + (min_ + step_ * ((1 << numBases_) - 1))
            + ", numBases=" + numBases_ + ", rawValue=" + rawValue_ + "}";
    }

    private final double min_;
    private final double step_;
}
