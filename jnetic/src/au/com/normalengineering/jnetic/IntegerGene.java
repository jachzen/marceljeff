package au.com.normalengineering.jnetic;

public class IntegerGene extends Gene implements IIntegerGene {
    private static final long serialVersionUID = 3628110916831314396L;

    public static IntegerGene getNewInstance(int min, int max) {
        return new IntegerGene(min, max, -1.0);
    }

    public static IntegerGene getNewInstance(int min, int max, double sd) {
        return new IntegerGene(min, max, sd);
    }

    public static IntegerGene getNewValueInstance(int value) {
        return new IntegerGene(value, value, value, -1.0);
    }

    private IntegerGene(int min, int max, double sd) {
        super(1);
        min_ = min;
        max_ = max;
        sd_ = sd;
        value_ = getRandomValue(min, max);
    }

    private IntegerGene(int value, int min, int max, double sd) {
        super(1);
        min_ = min;
        max_ = max;
        sd_ = sd;
        value_ = value;
    }

    protected IntegerGene(IntegerGene gene, int value) {
        super(1);
        min_ = gene.min_;
        max_ = gene.max_;
        sd_ = gene.sd_;
        value_ = value;
    }

    private static int getRandomValue(int min, int max) {
        return Random.getInstance().nextInt(max - min + 1) + min;
    }

    private static int getRandomValue(int min, int max, int oldValue, double sd) {
        double value = Random.getInstance().nextGaussian() * sd + oldValue;
        value = Math.min(max, value);
        value = Math.max(min, value);
        return (int) Math.round(value);
    }

    @Override
    IntegerGene getCrossedOverInstance(Gene other, int firstBase, int numBases) {
        return (IntegerGene) other;
    }

    @Override
    IntegerGene getMutatedInstance(int base) {
        if (sd_ <= 0) {
            return new IntegerGene(getRandomValue(min_, max_), min_, max_, sd_);
        } else {
            return new IntegerGene(getRandomValue(min_, max_, value_, sd_), min_, max_, sd_);
        }
    }

    @Override
    public int getValue() {
        return value_;
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", min=" + min_ + ", max=" + max_ + "}";
    }

    private final int min_;
    private final int max_;
    private final double sd_;
    private final int value_;
}
