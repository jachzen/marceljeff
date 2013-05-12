package au.com.normalengineering.jnetic;

public class BitGene extends Gene {
    private static final long serialVersionUID = -7500771402107288441L;

    @Override
    BitGene getCrossedOverInstance(Gene other, int firstBase, int numBases) {
        int maskOther = ((1 << Math.max(numBases + firstBase - numBases_, 0)) - 1)
            | (((1 << Math.min(numBases_ - firstBase, numBases)) - 1) << firstBase);
        int maskThis = maskOther ^ ((1 << numBases_) - 1);
        try {
            return getClass().getDeclaredConstructor(getClass(), int.class).newInstance(this,
                (((BitGene)other).rawValue_ & maskOther) | (rawValue_ & maskThis));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    BitGene getMutatedInstance(int base) {
        try {
            return getClass().getDeclaredConstructor(getClass(), int.class).newInstance(this, rawValue_ ^ (1 << base));
        } catch (Exception e) {
            return null;
        }
    }

    protected BitGene(int numBases, int rawValue) {
        super(numBases);
        rawValue_ = rawValue;
    }

    @Override
    public String toString() {
        return "{numBases=" + numBases_ + ", rawValue=" + rawValue_ + "}";
    }

    protected final int rawValue_;
}
