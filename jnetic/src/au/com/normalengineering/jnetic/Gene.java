package au.com.normalengineering.jnetic;

import java.io.Serializable;

public abstract class Gene implements Serializable {
    private static final long serialVersionUID = -4791487758297953910L;

    abstract Gene getCrossedOverInstance(Gene other, int firstBase, int numBases);

    abstract Gene getMutatedInstance(int base);

    int getNumBases() {
        return numBases_;
    }

    protected Gene(int numBases) {
        numBases_ = numBases;
        id_ = idCount_++;
    }

    @Override
    public String toString() {
        return "{numBases=" + numBases_ + "}";
    }

    protected final int numBases_;
    protected final int id_;
    static int idCount_ = 0;
}
