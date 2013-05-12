public interface IConfiguration {

	public static final String[] DATABASES = { "./data/mtgoxEUR.csv" };
    public static Class<?> ALGORITHM_CLASS = Algorithm1.class;
	public static Class<?> EXCHANGE_SIM_CLASS = ExchangeSimulator.class;
	public static Class<?> EXCHANGE_REAL_CLASS = ExchangeMtGox.class;
	public static Class<?> ENGINE_CLASS = Engine.class;
	//TODO configure
	public static double BALANCE = 1000;
	public static double FEE_PERCENTAGE = 0.6;
	public static long TIMER_PERIOD_SEC = 60;

    /* Set this to a file if you want the algorithm to output debug stuff to a
     * file. */
    public static String ALGORITHM_OUTPUT = null; // "algorithmOutput.csv";
}
