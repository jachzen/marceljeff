package au.com.normalengineering.jnetic;

import java.util.Random;

@SuppressWarnings("serial")
public class RandomStub extends Random {

    public RandomStub(int[] intArray) {
        intArray_ = intArray;
    }

    public RandomStub(double[] doubleArray) {
        doubleArray_ = doubleArray;
    }

    public RandomStub(int[] intArray, double[] doubleArray) {
        intArray_ = intArray;
        doubleArray_ = doubleArray;
    }

    @Override
    public int nextInt(int n) {
        return intArray_[iInt_++];
    }

    @Override
    public double nextDouble() {
        return doubleArray_[iDouble_++];
    }

    private int iInt_ = 0;
    private int iDouble_ = 0;
    private int[] intArray_ = null;
    private double[] doubleArray_ = null;
}
