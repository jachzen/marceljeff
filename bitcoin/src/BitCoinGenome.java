import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import au.com.normalengineering.jnetic.Genome;

public abstract class BitCoinGenome extends Genome {
    private static final long serialVersionUID = -8771840797304053457L;
    private final static Logger logger = Logger.getLogger(Engine.class.getName());

    /**
     * Base constructor.
     */
    public BitCoinGenome() {
    }

    /**
     * Set the exchange and database to use.
     */
    public static void setExchangeFactory(Class<IExchange> exchangeClass, Database database) {
        exchangeClass_ = exchangeClass;
        database_ = database;
    }

    /**
     * Set the engine to use.
     */
    public static void setEngineFactory(Class<IEngine> engineClass) {
        engineClass_ = engineClass;
    }

    /**
     * Resets processing of database. Call this each time before the first call
     * to run().
     */
    public abstract void resetProcessing(Ticker ticker);

    /**
     * Processes a ticker, returning the order suggestion.
     */
    public abstract OrderType addTicker(Ticker ticker, ArrayList<Double> debugData) throws IOException;

    @Override
    public double getRealFitness() {
        return simulate(null);
    }

    public double simulate(FileWriter writer) {

        double depot = 0; // default

        try {
            IExchange exchange = exchangeClass_.getConstructor(Database.class).newInstance(database_);
            IEngine engine = engineClass_.getConstructor(IExchange.class, BitCoinGenome.class).newInstance(exchange,
                this);

            resetProcessing(exchange.getTicker());

            long minuteCount = 0;
            Ticker ticker = null;
            for (;;) {
                /* When simulating (or optimizing), we know that the exchange
                 * must be a simulator, and each pass through the simulation
                 * loop the exchange must be incremented. */
                if (!((ExchangeSimulator) exchange).increment()) {
                    break;
                }

                /* Get the current ticker. */
                ticker = exchange.getTicker();

                /* Run the engine. */
                ArrayList<Double> debugData = new ArrayList<Double>();
                engine.run(debugData);

                final int OUTPUTS_PER_DAY = 24;
                if (writer != null && minuteCount++ % (24 * 60 / OUTPUTS_PER_DAY) == 0) {
                    double intermediateDepot = exchange.getWallet().getBalance() + exchange.getWallet().getBitCoins()
                        * ticker.getPrice();
                    writer.write(String.format("%s", new SimpleDateFormat("dd.MM.yyyy").format(ticker.getDate())));
                    writer.write(String.format(",%f", ticker.getPrice()));
                    writer.write(String.format(",%f", intermediateDepot));
                    for (Double d : debugData) {
                        writer.write(String.format(",%f", d));
                    }
                    writer.write("\n");
                }
            }

            /* Sell up and see how rich we are. */
            depot = exchange.getWallet().getBalance() + exchange.getWallet().getBitCoins() * ticker.getPrice();

        } catch (Exception e) {
            logger.error("", e);
        }

        return depot;
    }

    private static Class<IExchange> exchangeClass_;
    private static Database database_;
    private static Class<IEngine> engineClass_;
}
