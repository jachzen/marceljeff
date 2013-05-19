import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.log4j.Logger;

public class Simulate {
    private final static Logger logger = Logger.getLogger(Engine.class.getName());

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try {
            logger.info("Starting simulation");

            /* Read in the historical database for which the genome is to be
             * optimized. */
            Database database = new Database();
            for (String file : IConfiguration.DATABASES) {
                logger.info(String.format("Loading database from: \"%s\"...", file));
                int lineCount = database.addDatabaseFile(new File(file));
                logger.info(String.format("...done (%d lines)", lineCount));
            }

            /* Tell the algorithm what exchange to use. */
            logger.info(String.format("Using exchange %s", IConfiguration.EXCHANGE_CLASS.getSimpleName()));
            Method setExchangeFactory = IConfiguration.ALGORITHM_CLASS.getMethod("setExchangeFactory",
                java.lang.Class.class, Database.class);
            setExchangeFactory.invoke(null, IConfiguration.EXCHANGE_CLASS, database);

            /* Construct the engine. */
            // logger.info(String.format("Using engine %s",
            // IConfiguration.EXCHANGE_CLASS.getSimpleName()));
            // IEngine engine = (IEngine)
            // IConfiguration.ENGINE_CLASS.getConstructor().newInstance();

            /* Load genome. */
            logger.info(String.format("Loading genome %s", IConfiguration.GENOME));
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(IConfiguration.GENOME));
            BitCoinGenome genome = (BitCoinGenome) in.readObject();
            in.close();
            logger.info("Genome: " + genome.toString());

            /* Simulate and output results. */
            logger.info(String.format("Simulating and outputting into %s", IConfiguration.SIMULATION_RESULTS));
            FileWriter writer = new FileWriter(new File(IConfiguration.SIMULATION_RESULTS));
            genome.simulate(writer);
            writer.close();

            logger.info("done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
