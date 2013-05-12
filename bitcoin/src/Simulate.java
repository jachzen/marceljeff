import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Simulate {
    private final static Logger logger = Logger.getLogger(Engine.class.getName());

    public static void main(String[] args) {
        Database database = new Database();
        try {
            for (String file : IConfiguration.DATABASES) {
                logger.info(String.format("Loading database from: \"%s\"", file));
                int lineCount = database.addDatabaseFile(new File(file));
                logger.info(String.format("(%d lines)", lineCount));
            }

            logger.info(String.format("Constructing exchange class: \"%s\"",
                IConfiguration.EXCHANGE_CLASS.getSimpleName()));
            IExchange exchange = (IExchange) IConfiguration.EXCHANGE_CLASS.getConstructor(Database.class).newInstance(
                database);

            logger.info(String.format("Constructing algorithm class: \"%s\"",
                IConfiguration.EXCHANGE_CLASS.getSimpleName()));
            IAlgorithm algorithm = (IAlgorithm) IConfiguration.ALGORITHM_CLASS.getConstructor().newInstance();
            if (IConfiguration.ALGORITHM_OUTPUT != null) {
                try {
                    FileWriter writer = new FileWriter(new File(IConfiguration.ALGORITHM_OUTPUT));
                    algorithm.setDebugFile(writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            logger.info(String.format("Constructing engine class: \"%s\"",
                IConfiguration.EXCHANGE_CLASS.getSimpleName()));
            IEngine engine = (IEngine) IConfiguration.ENGINE_CLASS.getConstructor().newInstance();

            logger.info("Runnng engine...");
            engine.run(exchange, algorithm);

            logger.info("...done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
