package au.com.normalengineering.jnetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class GeneTest {

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
    public void testGeneNumBases() {

        RawBitGene rawBitGene;

        try {
            random_.set(null, new RandomStub(new int[] { 0x00 }));
            rawBitGene = RawBitGene.getNewInstance(8);
            assertEquals(8, rawBitGene.getNumBases());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBitGeneCrossover() {

        RawBitGene rawBitGene;
        final RawBitGene rawGene1;
        final RawBitGene rawGene2;

        try {
            random_.set(null, new RandomStub(new int[] { 0x00, 0xFF }));
            rawGene1 = RawBitGene.getNewInstance(8);
            rawGene2 = RawBitGene.getNewInstance(8);
            assertEquals(0x00, rawGene1.getValue());
            assertEquals(0xFF, rawGene2.getValue());

            rawBitGene = (RawBitGene) rawGene1.getCrossedOverInstance(rawGene2, 5, 6);
            assertEquals(0xE7, rawBitGene.getValue());

            rawBitGene = (RawBitGene) rawGene2.getCrossedOverInstance(rawGene1, 3, 2);
            assertEquals(0xE7, rawBitGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBitGeneMutation() {

        RawBitGene rawBitGene;

        try {
            random_.set(null, new RandomStub(new int[] { 0x00 }));
            rawBitGene = RawBitGene.getNewInstance(8);
            assertEquals(0x00, rawBitGene.getValue());

            rawBitGene = (RawBitGene) rawBitGene.getMutatedInstance(0);
            assertEquals(0x01, rawBitGene.getValue());

            rawBitGene = (RawBitGene) rawBitGene.getMutatedInstance(1);
            assertEquals(0x03, rawBitGene.getValue());

            rawBitGene = (RawBitGene) rawBitGene.getMutatedInstance(7);
            assertEquals(0x83, rawBitGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testDoubleBitGene() {

        DoubleBitGene doubleBitGene;

        try {
            random_.set(null, new RandomStub(new int[] { 0 }));
            doubleBitGene = DoubleBitGene.getNewInstance(10.0, 13.0, 2);
            assertEquals(2, doubleBitGene.getNumBases());
            assertDoubleEquals(10.0, doubleBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 1 }));
            doubleBitGene = DoubleBitGene.getNewInstance(10.0, 13.0, 2);
            assertDoubleEquals(11.0, doubleBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 2 }));
            doubleBitGene = DoubleBitGene.getNewInstance(10.0, 13.0, 2);
            assertDoubleEquals(12.0, doubleBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 3 }));
            doubleBitGene = DoubleBitGene.getNewInstance(10.0, 13.0, 2);
            assertDoubleEquals(13.0, doubleBitGene.getValue());

            /* Test: The protected constructor. */
            doubleBitGene = (DoubleBitGene) doubleBitGene.getMutatedInstance(0);
            assertDoubleEquals(12.0, doubleBitGene.getValue());

            doubleBitGene = DoubleBitGene.getNewValueInstance(11.8);
            assertDoubleEquals(11.8, doubleBitGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testIntegerBitGene() {

        IntegerBitGene integerBitGene;

        try {
            random_.set(null, new RandomStub(new int[] { 0 }));
            integerBitGene = IntegerBitGene.getNewInstance(10, 13);
            assertEquals(2, integerBitGene.getNumBases());
            assertEquals(10, integerBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 1 }));
            integerBitGene = IntegerBitGene.getNewInstance(10, 13);
            assertEquals(11, integerBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 2 }));
            integerBitGene = IntegerBitGene.getNewInstance(10, 13);
            assertEquals(12, integerBitGene.getValue());

            random_.set(null, new RandomStub(new int[] { 3 }));
            integerBitGene = IntegerBitGene.getNewInstance(10, 13);
            assertEquals(13, integerBitGene.getValue());

            /* Test: The protected constructor. */
            integerBitGene = (IntegerBitGene) integerBitGene.getMutatedInstance(0);
            assertEquals(12, integerBitGene.getValue());

            integerBitGene = IntegerBitGene.getNewValueInstance(12);
            assertEquals(12, integerBitGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testDoubleGene() {

        IDoubleGene doubleGene;
        final IDoubleGene doubleGene1;
        final IDoubleGene doubleGene2;

        try {
            random_.set(null, new RandomStub(new double[] { 0.1, 0.2, 0.3 }));
            doubleGene1 = DoubleGene.getNewInstance(10.0, 13.0);
            doubleGene2 = DoubleGene.getNewInstance(10.0, 13.0);
            assertEquals(1, ((Gene) doubleGene1).getNumBases());
            assertDoubleEquals(10.3, doubleGene1.getValue());
            assertDoubleEquals(10.6, doubleGene2.getValue());

            doubleGene = (IDoubleGene) ((Gene) doubleGene1).getMutatedInstance(0);
            assertDoubleEquals(10.9, doubleGene.getValue());

            doubleGene = (IDoubleGene) ((Gene) doubleGene1).getCrossedOverInstance((Gene) doubleGene2, 0, 1);
            assertDoubleEquals(10.6, doubleGene.getValue());

            doubleGene = DoubleGene.getNewValueInstance(11.8);
            assertDoubleEquals(11.8, doubleGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testIntegerGene() {

        IIntegerGene integerGene;
        final IIntegerGene integerGene1;
        final IIntegerGene integerGene2;

        try {
            random_.set(null, new RandomStub(new int[] { 0, 1, 2 }));
            integerGene1 = IntegerGene.getNewInstance(10, 13);
            integerGene2 = IntegerGene.getNewInstance(10, 13);
            assertEquals(1, ((Gene) integerGene1).getNumBases());
            assertEquals(10, integerGene1.getValue());
            assertEquals(11, integerGene2.getValue());

            integerGene = (IIntegerGene) ((Gene) integerGene1).getMutatedInstance(0);
            assertEquals(12, integerGene.getValue());

            integerGene = (IIntegerGene) ((Gene) integerGene1).getCrossedOverInstance((Gene) integerGene2, 0, 1);
            assertEquals(11, integerGene.getValue());

            integerGene = IntegerGene.getNewValueInstance(11);
            assertEquals(11, integerGene.getValue());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void assertDoubleEquals(double expected, double actual) {
        assertTrue(expected > (actual - 0.001));
        assertTrue(expected < (actual + 0.001));
    }

    private Field random_;
}
