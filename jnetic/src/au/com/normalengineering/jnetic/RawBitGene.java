package au.com.normalengineering.jnetic;

public class RawBitGene extends BitGene {
    private static final long serialVersionUID = -3139176509012312928L;

    public static RawBitGene getNewInstance(int numBits) {
        return new RawBitGene(numBits);
    }

    private RawBitGene(int numBases) {
        super(numBases, Random.getInstance().nextInt(1 << numBases));
    }

    protected RawBitGene(RawBitGene gene, int rawValue) {
        super(gene.numBases_, rawValue);
    }

    public int getValue() {
        return rawValue_;
    }
}
