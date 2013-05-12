package au.com.normalengineering.jnetic;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GenomeTest {

    @Before
    public void before() {
        try {
            random_ = Random.class.getDeclaredField("random_");
        } catch (Exception e) {
            fail(e.toString());
        }
        random_.setAccessible(true);
    }

    @Test
    public void testGenomeSort() {

        final ArrayList<Genome> genomes = new ArrayList<Genome>();

        try {
            random_.set(null, new RandomStub(new int[] { 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1 }));
            genomes.add(new GenomeStub());
            genomes.add(new GenomeStub());
            genomes.add(new GenomeStub());
            assertDoubleEquals(15.0, genomes.get(0).getFitness());
            assertDoubleEquals(10.0, genomes.get(1).getFitness());
            assertDoubleEquals(5.0, genomes.get(2).getFitness());

            /* Test: compareTo() is order of decreasing fitness. */
            assertTrue(genomes.get(1).compareTo(genomes.get(0)) > 0);
            assertTrue(genomes.get(1).compareTo(genomes.get(1)) == 0);
            assertTrue(genomes.get(1).compareTo(genomes.get(2)) < 0);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGenomeCrossover() {

        Genome genome;
        final Genome genome1;
        final Genome genome2;

        try {
            random_.set(null, new RandomStub(new int[] { 2, 0, 0, 0, 0, 15, 3, 13, 7, 1 }));
            genome1 = new GenomeStub();
            genome2 = new GenomeStub();
            assertDoubleEquals(2.0, genome1.getFitness());
            assertDoubleEquals(39.0, genome2.getFitness());

            genome = genome1.getCrossedOverInstance(genome2, 5, 7);
            assertDoubleEquals(20.0, genome.getFitness());

            genome = genome2.getCrossedOverInstance(genome1, 12, 7);
            assertDoubleEquals(20.0, genome.getFitness());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGenomeMutation() {

        Genome genome;

        try {
            random_.set(null, new RandomStub(new int[] { 0, 0, 0, 0, 0 }));
            genome = new GenomeStub();
            assertDoubleEquals(0.0, genome.getFitness());

            genome = genome.getMutatedInstance(0);
            assertDoubleEquals(1.0, genome.getFitness());

            genome = genome.getMutatedInstance(1);
            assertDoubleEquals(3.0, genome.getFitness());

            genome = genome.getMutatedInstance(13);
            assertDoubleEquals(4.0, genome.getFitness());

            genome = genome.getMutatedInstance(12);
            assertDoubleEquals(8.0, genome.getFitness());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void assertDoubleEquals(double expected, double actual) {
        assertTrue(expected > (actual - 0.001));
        assertTrue(expected < (actual + 0.001));
    }

    static class GenomeStub extends Genome {
        private static final long serialVersionUID = -1092623416026882525L;

        public GenomeStub() {
            genes_ = new ArrayList<Gene>();
            genes_.add(RawBitGene.getNewInstance(4));
            genes_.add(RawBitGene.getNewInstance(2));
            genes_.add(RawBitGene.getNewInstance(4));
            genes_.add(RawBitGene.getNewInstance(3));
            genes_.add(RawBitGene.getNewInstance(1));
        }

        protected GenomeStub(GenomeStub genome, final ArrayList<Gene> genes) {
            genes_ = genes;
        }

        @Override
        public double getRealFitness() {
            double fitness = 0.0;
            for (Gene gene : genes_) {
                fitness += ((RawBitGene) gene).getValue();
            }
            return fitness;
        }
    }

    private Field random_;
}
