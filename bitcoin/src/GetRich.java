import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class GetRich {

	private final static Logger logger = Logger.getLogger(Engine.class.getName());

	public static void main(String[] args) {
		try {

			logger.info(String.format("Constructing exchange class: \"%s\"", IConfiguration.EXCHANGE_REAL_CLASS.getSimpleName()));
			final IExchange exchange = (IExchange) IConfiguration.EXCHANGE_REAL_CLASS.getConstructor().newInstance();

            /* Load genome. */
            logger.info(String.format("Loading genome %s", IConfiguration.GENOME));
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(IConfiguration.GENOME));
            BitCoinGenome genome = (BitCoinGenome) in.readObject();
            in.close();
            logger.info("Genome: " + genome.toString());

			logger.info(String.format("Constructing engine class: \"%s\"", IConfiguration.EXCHANGE_REAL_CLASS.getSimpleName()));
			final IEngine engine = (IEngine) IConfiguration.ENGINE_CLASS.getConstructor(IExchange.class, BitCoinGenome.class).newInstance(exchange, genome);

			logger.info("Runnng engine...");
			final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					engine.run();
				}
			}, 0, IConfiguration.TIMER_PERIOD_SEC, TimeUnit.SECONDS);
			scheduler.awaitTermination(1000000, TimeUnit.DAYS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
