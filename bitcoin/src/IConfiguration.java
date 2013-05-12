public interface IConfiguration {

    public static final String[] DATABASES = { "./data/alldata.csv" };
    public static Class<?> ALGORITHM_CLASS = Algorithm1.class;
    public static Class<?> EXCHANGE_CLASS = ExchangeSimulator.class;
    public static Class<?> ENGINE_CLASS = Engine.class;

    /* Set this to a file if you want the algorithm to output debug stuff to a
     * file. */
    public static String ALGORITHM_OUTPUT = null; // "algorithmOutput.csv";

    // TODO configure
    public static double BALANCE = 1000;
    public static double FEE_PERCENTAGE = 0.6;
}
