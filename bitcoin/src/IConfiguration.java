public interface IConfiguration {

    public static Class<?> EXCHANGE_REAL_CLASS = ExchangeMtGox.class;
    public static long TIMER_PERIOD_SEC = 60;

    /* Engine to use for optimization, simulation and live trading. */
    public static Class<?> ENGINE_CLASS = Engine.class;

    /* For simulation and optimization. */
    public static final String[] DATABASES = { "./data/mtgoxEUR-ab20Apr.csv" };
    public static Class<?> EXCHANGE_CLASS = ExchangeSimulator.class;
    public static String SIMULATION_RESULTS = "./genomes/simulationResults.csv";

    /* Set this to a file if you want the algorithm to output debug stuff to a
     * file. */
    public static String ALGORITHM_OUTPUT = null; // "./genomes/algorithmOutput.csv";

    /* Genetic optimization control. */
    public static Class<?> ALGORITHM_CLASS = BitCoinGenome3.class;
    public static final int MAX_ALLOWED_EVOLUTIONS = 51;
    public static final double CROSSOVER_RATE = 0.35d;
    public static final int POPULATION_SIZE = 100;
    public static final String FITTEST_GENOME = "./genomes/" + ALGORITHM_CLASS.getSimpleName() + "_"
        + MAX_ALLOWED_EVOLUTIONS + ".ser";

    /* Genome to use for simulation and live trading. */
    public static final String GENOME = "./genomes/BitCoinGenome2_10-minute-transactions.ser";


    // TODO configure
	public static double BALANCE = 100;
    public static double FEE_PERCENTAGE = 0.6;
}
