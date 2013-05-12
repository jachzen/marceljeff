package au.com.normalengineering.jnetic;

public class Random {

    public static java.util.Random getInstance() {
        return random_;
    }

    private static java.util.Random random_ = new java.util.Random();
}
