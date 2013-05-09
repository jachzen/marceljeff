import static org.junit.Assert.*;

import org.junit.Test;


public class AlgorithmsTest {

    @Test
    public void test() {
        final double initialValue = 1.0;
        double currentAverage = initialValue;
        final double newValue = 2.0;
        final double halfLife = 4.0;
        double scaledAverage;
        int i = 0;
        currentAverage = Algorithms.nextMovingAverageHl(currentAverage, newValue, halfLife);
        scaledAverage = Algorithms.nextMovingAverageScaled(initialValue, newValue, halfLife, ++i);
        assertDoubleEquals(currentAverage, scaledAverage);
        currentAverage = Algorithms.nextMovingAverageHl(currentAverage, newValue, halfLife);
        scaledAverage = Algorithms.nextMovingAverageScaled(initialValue, newValue, halfLife, ++i);
        assertDoubleEquals(currentAverage, scaledAverage);
        currentAverage = Algorithms.nextMovingAverageHl(currentAverage, newValue, halfLife);
        scaledAverage = Algorithms.nextMovingAverageScaled(initialValue, newValue, halfLife, ++i);
        assertDoubleEquals(currentAverage, scaledAverage);
        currentAverage = Algorithms.nextMovingAverageHl(currentAverage, newValue, halfLife);
        scaledAverage = Algorithms.nextMovingAverageScaled(initialValue, newValue, halfLife, ++i);
        assertDoubleEquals(currentAverage, scaledAverage);
    }

    private void assertDoubleEquals(double expected, double actual) {
        assertTrue(expected > (actual - 0.000001));
        assertTrue(expected < (actual + 0.000001));
    }
}
