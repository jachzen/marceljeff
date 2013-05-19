import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
     * Resets processing of database. Call this each time before the first call
     * to run().
     */
    public abstract void resetProcessing(Ticker ticker);

    /**
     * Processes a ticker, returning the order suggestion.
     */
    public abstract OrderType addTicker(Ticker ticker) throws IOException;

    @Override
    public double getRealFitness() {

        double depot = 0; // default

        try {
            IExchange exchange = exchangeClass_.getConstructor(Database.class).newInstance(database_);
            if (IConfiguration.START_WITH_BITCOINS) {
                exchange.placeBuyOrder(new NormalOrder());
            }

            resetProcessing(exchange.getTicker());

            Ticker ticker = null;
            for (;;) {
                // TODO Jeff: Call Marcel's Engine here.
                if (!((ExchangeSimulator) exchange).increment()) {
                    break;
                }
                ticker = exchange.getTicker();
                OrderType intend = addTicker(ticker);
                switch (intend) {
                case BUY:
                    exchange.placeBuyOrder(new NormalOrder());
                    break;
                case SELL:
                    exchange.placeSellOrder(new NormalOrder());
                    break;
                case DO_NOTHING:
                    break;
                }
            }

            /* Sell up and see how rich we are. */
            depot = exchange.getWallet().getBalance() + exchange.getWallet().getBitCoins() * ticker.getPrice();

        } catch (Exception e) {
            logger.error("", e);
        }
        return depot;
    }

    public void simulate(FileWriter writer) {

        try {
            IExchange exchange = exchangeClass_.getConstructor(Database.class).newInstance(database_);
            if (IConfiguration.START_WITH_BITCOINS) {
                exchange.placeBuyOrder(new NormalOrder());
            }

            resetProcessing(exchange.getTicker());

            long minuteCount = 0;
            for (;;) {
                // TODO Jeff: Call Marcel's Engine here.
                if (!((ExchangeSimulator) exchange).increment()) {
                    break;
                }
                OrderType intend = addTicker(exchange.getTicker());
                switch (intend) {
                case BUY:
                    exchange.placeBuyOrder(new NormalOrder());
                    break;
                case SELL:
                    exchange.placeSellOrder(new NormalOrder());
                    break;
                case DO_NOTHING:
                    break;
                }
                if (minuteCount++ % (24 * 60 / 240) == 0) {
                    double depot = exchange.getWallet().getBalance() + exchange.getWallet().getBitCoins()
                        * exchange.getTicker().getPrice();
                    writer.write(String.format("%s,%f,%f\n", new SimpleDateFormat("dd.MM.yyyy").format(exchange
                        .getTicker().getDate()), exchange.getTicker().getPrice(), depot));
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private static Class<IExchange> exchangeClass_;
    private static Database database_;
}
