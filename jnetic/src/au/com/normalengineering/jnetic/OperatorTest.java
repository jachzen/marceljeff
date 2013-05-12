package au.com.normalengineering.jnetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class OperatorTest {

    @Before
    public void before() {
        genomes_.add(new GenomeStub("A", 3.0));
        genomes_.add(new GenomeStub("B", 2.0));
        genomes_.add(new GenomeStub("C", 1.0));
        genomes_.add(new GenomeStub("D", 0.0));

        try {
            random_ = Random.class.getDeclaredField("random_");
        } catch (Exception e) {
            fail(e.toString());
        }
        random_.setAccessible(true);
    }

    @Test
    public void testSelection() {

        ArrayList<Genome> newGenomes;

        try {
            random_.set(null, new RandomStub(new double[] { 0.1 }));
            newGenomes = Selection.stochasticUniversalSampling(genomes_);
            assertEquals("A", newGenomes.get(0).toString());
            assertEquals("A", newGenomes.get(1).toString());
            assertEquals("B", newGenomes.get(2).toString());
            assertEquals("B", newGenomes.get(3).toString());

            random_.set(null, new RandomStub(new double[] { 0.9 }));
            newGenomes = Selection.stochasticUniversalSampling(genomes_);
            assertEquals("A", newGenomes.get(0).toString());
            assertEquals("A", newGenomes.get(1).toString());
            assertEquals("B", newGenomes.get(2).toString());
            assertEquals("C", newGenomes.get(3).toString());

            random_.set(null, new RandomStub(new double[] { 0.1, 0.3, 0.4, 0.7 }));
            newGenomes = Selection.rouletteWheel(genomes_);
            assertEquals("A", newGenomes.get(0).toString());
            assertEquals("A", newGenomes.get(1).toString());
            assertEquals("A", newGenomes.get(2).toString());
            assertEquals("B", newGenomes.get(3).toString());

            random_.set(null, new RandomStub(new double[] { 0.1, 0.6, 0.9, 0.9 }));
            newGenomes = Selection.rouletteWheel(genomes_);
            assertEquals("A", newGenomes.get(0).toString());
            assertEquals("B", newGenomes.get(1).toString());
            assertEquals("C", newGenomes.get(2).toString());
            assertEquals("C", newGenomes.get(3).toString());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCrossover() {

        ArrayList<Genome> newGenomes;

        try {
            /* RandomStub parameters work as follows: */
            /* - 3,0,0 shuffle genomes_ from A,B,C,D to B,C,A,D. */
            /* - 0.4 causes crossover of B,C with firstBase 1 and numBases 2. */
            /* - 0.6 prevents crossover of A,D. */
            random_.set(null, new RandomStub(new int[] { 3, 0, 0, 1, 2 }, new double[] { 0.4, 0.6 }));
            newGenomes = Crossover.twoPoint(genomes_, 0.5);
            assertEquals("BC12", newGenomes.get(0).toString());
            assertEquals("CB12", newGenomes.get(1).toString());
            assertEquals("A", newGenomes.get(2).toString());
            assertEquals("D", newGenomes.get(3).toString());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testMutation() {

        ArrayList<Genome> newGenomes;

        try {
            /* RandomStub parameters work as follows: */
            /* - 0.4,0.6,0.6 causes A mutation of base 0. */
            /* - 0.6,0.4,0.4 causes B mutation of bases 1,2. */
            /* - 0.6,0.6,0.6 causes no C mutation. */
            /* - 0.4,0.4,0.4 causes D mutation of bases 0,1,2. */
            random_.set(null, new RandomStub(new double[] { 0.4, 0.6, 0.6, 0.6, 0.4, 0.4, 0.6, 0.6, 0.6, 0.4, 0.4, 0.4 }));
            newGenomes = Mutation.bitInversion(genomes_, 0.5);
            assertEquals("A0", newGenomes.get(0).toString());
            assertEquals("B12", newGenomes.get(1).toString());
            assertEquals("C", newGenomes.get(2).toString());
            assertEquals("D012", newGenomes.get(3).toString());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    static class GenomeStub extends Genome {
        private static final long serialVersionUID = 4539113905108577228L;

        public GenomeStub(String name, double fitness) {
            name_ = name;
            fitness_ = fitness;
        }

        @Override
        public Genome getCrossedOverInstance(Genome other, int firstBase, int numBases) {
            return new GenomeStub(name_ + ((GenomeStub) other).name_ + firstBase + numBases, fitness_);
        }

        @Override
        public Genome getMutatedInstance(int base) {
            return new GenomeStub(name_ + base, fitness_);
        }

        @Override
        public int getNumBases() {
            return 3;
        }

        @Override
        protected double getRealFitness() {
            return fitness_;
        }

        @Override
        public String toString() {
            return name_;
        }

        String name_;
        double fitness_;
    }

    private final ArrayList<Genome> genomes_ = new ArrayList<Genome>();
    private Field random_;
}
