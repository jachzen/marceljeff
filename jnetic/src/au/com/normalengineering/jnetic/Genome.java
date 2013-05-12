package au.com.normalengineering.jnetic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/* Note: This class has a natural ordering that is inconsistent with equals().
 * If this is a problem you simply to override compareTo(), equals() and
 * hashCode() as appropriate for your case. */
/* Note: Whenever this class is changed, it is very important to check the
 * contained SerializationProxy to see if it needs change to keep backwards
 * compatibility. */
public abstract class Genome implements Comparable<Genome>, Serializable {
    private static final long serialVersionUID = -4918462494579483768L;

    public Genome() {
        id_ = idCount_++;
    }

    public Genome getCrossedOverInstance(Genome other, int firstBase, int numBases) {
        ArrayList<Gene> newGenes = new ArrayList<Gene>();
        int baseCount = 0;
        int crossedCount = 0;
        for (int i = 0; i < genes_.size(); i++) {
            Gene gene = genes_.get(i);
            int thisFirstBase = mod(firstBase - baseCount, getNumBases());
            if (thisFirstBase >= gene.getNumBases()) {
                thisFirstBase = 0;
            }
            int thisNumBases = Math.min(gene.getNumBases() - Math.max(firstBase - baseCount, 0), numBases
                - crossedCount);
            if (thisNumBases < 0) {
                thisNumBases = Math.min(firstBase + numBases - getNumBases() - crossedCount, gene.getNumBases());
            }
            if (thisNumBases > 0) {
                newGenes.add(gene.getCrossedOverInstance(other.genes_.get(i), thisFirstBase, thisNumBases));
                crossedCount += thisNumBases;
            } else {
                newGenes.add(gene);
            }
            baseCount += gene.getNumBases();
        }
        try {
            return getClass().getDeclaredConstructor(getClass(), ArrayList.class).newInstance(this, newGenes);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor " + getClass().getSimpleName() + "(" + getClass().getSimpleName()
                + " genome, ArrayList<Gene> genes) does not exist");
            return null;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public Genome getMutatedInstance(int base) {
        @SuppressWarnings("unchecked")
        ArrayList<Gene> newGenes = (ArrayList<Gene>) genes_.clone();
        for (int i = 0; i < genes_.size(); i++) {
            Gene gene = genes_.get(i);
            if (base < gene.getNumBases()) {
                newGenes.set(i, gene.getMutatedInstance(base));
                break;
            }
            base -= gene.getNumBases();
        }
        try {
            return getClass().getDeclaredConstructor(getClass(), ArrayList.class).newInstance(this, newGenes);
        } catch (Exception e) {
            return null;
        }
    }

    public Genome getInstance(ArrayList<Gene> genes) {
        try {
            return getClass().getDeclaredConstructor(getClass(), ArrayList.class).newInstance(this, genes);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor " + getClass().getSimpleName() + "(" + getClass().getSimpleName()
                + " genome, ArrayList<Gene> genes) does not exist");
            return null;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public int getNumBases() {
        if (!numBasesValid_) {
            numBases_ = 0;
            for (Gene gene : genes_) {
                numBases_ += gene.getNumBases();
            }
        }
        return numBases_;
    }

    public double getFitness() {
        if (!fitnessValid_) {
            fitness_ = getRealFitness();
            fitnessValid_ = true;
        }
        return fitness_;
    }

    protected abstract double getRealFitness();

    ArrayList<Gene> getGenes() {
        return genes_;
    }

    @Override
    public int compareTo(Genome obj) {
        /* Note: This sorts in order of decreasing fitness; that is compareTo()
         * compares negative fitness. */
        if (getFitness() == obj.getFitness()) {
            return 0;
        } else if (obj.getFitness() > getFitness()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "{id=" + id_ + ", genes=" + genes_ + ", fitness=" + getFitness() + "}";
    }

    private int mod(int n, int d) {
        return n - (n < 0 ? (n + 1) / d - 1 : n / d) * d;
    }

    /**
     * Serialization proxy for objects of classes derived from Genome.
     *
     * Note: Whenever the Genome class changes, this SerializationProxy must be
     * examined to see if it needs a version change. The idea is that, no matter
     * what changes, the gene array should be able to be read from older file
     * versions and the Filter object reconstructed from that.
     *
     * A proxy is used for several reasons:
     *
     * - It gives full control over how the objects are serialized and
     * deserialized, which allows for backwards compatibility for example.
     *
     * - It allows the actual objects to be come from derived classes, without
     * tying the serialization protocol to this implementation detail.
     *
     * - It allows better control over the construction of the deserialized
     * objects.
     */
    protected static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -7595048643894930390L;

        /**
         * This constant is used to version the written serialization stream.
         * Refer to comments below in readObject().
         */
        private final static int VERSION = 1;

        /**
         * Constructs a proxy object based on the specified Genome.
         *
         * @param genome
         *            The Genome object to construct a proxy for.
         */
        SerializationProxy(Genome genome) {
            class_ = genome.getClass();
            genes_ = genome.genes_;
        }

        /**
         * This constructs and returns a new Genome object based on this
         * proxy.
         *
         * @throws NoSuchMethodException
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         * @throws InstantiationException
         * @throws SecurityException
         * @throws IllegalArgumentException
         */
        private Object readResolve() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return class_.getConstructor(ArrayList.class).newInstance(genes_);
        }

        /**
         * Writes this proxy object to the specified output stream.
         *
         * For information on the various output formats see comments in
         * readObject().
         *
         * @param out
         *            Output stream to write to.
         * @throws IOException
         *             Any exception thrown by the underlying OutputStream.
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeInt(VERSION);
            switch (VERSION) {
            case 1:
                out.writeObject(class_);
                out.writeObject(genes_);
                break;
            default:
                throw new IOException("Illegal version=" + VERSION);
            }
        }

        /**
         * Reads this object from the specified input stream.
         *
         * @param in
         *            The input stream to read from.
         * @throws ClassNotFoundException
         *             Class of a serialized object cannot be found.
         * @throws IOException
         *             Any of the usual Input/Output related exceptions.
         */
        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            int version = in.readInt();
            switch (version) {
            case 1:
                // Version 1 stores class and gene array.
                class_ = (Class<?>) in.readObject();
                genes_ = (ArrayList<Gene>) in.readObject();
                break;
            default:
                throw new IOException("Illegal version=" + VERSION);
            }
        }

        private Class<?> class_;
        private ArrayList<Gene> genes_;
    }

    /**
     * Nominates a SerializationProxy object as a replacement for this
     * Genome object in the stream before the object is written.
     *
     * @return The SerializationProxy object to use as a replacement for this
     *         Genome object.
     */
    protected Object writeReplace() {
        return new SerializationProxy(this);
    }

    /**
     * This method is called to read a Genome from the ObjectInputStream.
     * Because the Genome object should have been replaced by a
     * SerializationProxy object, this method should never be called. This
     * method is here to catch a programming error which may result in a
     * Genome object being erroneously written to a stream.
     *
     * @param in
     *            The input stream to read from.
     * @throws IOException
     */
    protected void readObject(ObjectInputStream in) throws IOException {
        throw new IOException("SerisalizatioProxy not called");
    }

    protected boolean fitnessValid_ = false;
    protected double fitness_;
    protected boolean numBasesValid_ = false;
    protected int numBases_;
    protected ArrayList<Gene> genes_;
    protected int id_;
    protected static int idCount_ = 0;
}
