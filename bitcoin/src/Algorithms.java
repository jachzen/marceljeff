/**
 * General algorithms for this project.
 */
public class Algorithms {

    /**
     * Returns the next value of an exponential moving average.
     *
     * @param currentAverage
     *        The existing moving average value.
     * @param newValue
     *        The new value to shift the average with.
     * @param alpha
     *        The degree of weighting decrease, 0-1. The higher this value, the
     *        faster the moving average. Half-life is around 2/(3*alpha) calls.
     * @return The next value of the moving average.
     */
    public static double nextMovingAverage(double currentAverage, double newValue, double alpha) {
        return alpha * newValue + (1.0 - alpha) * currentAverage;
    }

    /**
     * Returns the next value of an exponential moving average.
     *
     * @param currentAverage
     *        The existing moving average value.
     * @param newValue
     *        The new value to shift the average with.
     * @param halfLife
     *        The half-life of the moving average.
     * @return The next value of the moving average.
     */
    public static double nextMovingAverageHl(double currentAverage, double newValue, double halfLife) {
        return nextMovingAverage(currentAverage, newValue, 2.0 / (2.9 * halfLife + 1));
    }

    /**
     * Returns the next value of an exponential moving average, specifying how
     * much time has elapsed since the last calculation.
     *
     * @param currentAverage
     *        The existing moving average value.
     * @param newValue
     *        The new value to shift the average with.
     * @param halfLife
     *        The half-life of the moving average, in the same units as time.
     * @param time
     *        The time since the last update, in the same units as halfLife.
     * @return The next value of the moving average.
     */
    public static double nextMovingAverageScaled(double currentAverage, double newValue, double halfLife, double time) {
        return nextMovingAverage(currentAverage, newValue, 1.0 - Math.pow(1.0 - 2.0 / (2.9 * halfLife + 1), time));
    }

    /**
     * Returns linear interpolation between two points.
     */
    public static double getLinearInterpolation(double x0, double x1, double y0, double y1, double x) {
        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }

    /**
     * Returns linear interpolation between with a multi-segment linear
     * relationship defined by an array of points.
     */
    public static double getMultiLinearInterpolation(double[] xx, double[] yy, double x) {
        for (int i = 1; i < xx.length - 1; i++) {
            if (x < xx[i]) {
                return getLinearInterpolation(xx[i - 1], xx[i], yy[i - 1], yy[i], x);
            }
        }
        return getLinearInterpolation(xx[xx.length - 2], xx[xx.length - 1], yy[xx.length - 2], yy[xx.length - 2], x);
    }

    /**
     * Returns linear interpolation between with a multi-segment linear
     * relationship defined by two fixed end points and a bunch of inbetween
     * points that maybe don't fit. You'll have to look at the code to work this
     * one out; I've forgotten exactly does.
     */
    public static double getMultiSegmentInterpolation(double x0, double xn, double y0, double yn, double[] dx,
        double[] dy, double x) {
        double sumX = 0.0;
        double sumY = 0.0;
        for (int i = 0; i < dx.length; i++) {
            sumX += dx[i];
            sumY += dy[i];
        }
        double[] xx = new double[dx.length + 1];
        double[] yy = new double[dy.length + 1];
        xx[0] = x0;
        yy[0] = y0;
        for (int i = 1; i < xx.length; i++) {
            xx[i] = xx[i - 1] + (xn - x0) * dx[i - 1] / sumX;
            yy[i] = yy[i - 1] + (yn - y0) * dy[i - 1] / sumY;
        }
        return getMultiLinearInterpolation(xx, yy, x);
    }

    /**
     * Returns linear interpolation between with a multi-segment linear
     * relationship defined by two fixed end points and a bunch of inbetween
     * points that must fit. You'll have to look at the code to work this one
     * out; I've forgotten exactly does.
     */
    public static double getMultiSegmentInterpolationNoNormalization(double x0, double xn, double y0, double yn,
        double[] dx, double[] dy, double x) {
        double[] xx = new double[dx.length + 1];
        double[] yy = new double[dy.length + 1];
        xx[0] = x0;
        yy[0] = y0;
        for (int i = 1; i < xx.length; i++) {
            xx[i] = xx[i - 1] + (xn - x0) * dx[i - 1];
            yy[i] = yy[i - 1] + (yn - y0) * dy[i - 1];
        }
        return getMultiLinearInterpolation(xx, yy, x);
    }
}
