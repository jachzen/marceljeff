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

			logger.info(String.format("Constructing algorithm class: \"%s\"", IConfiguration.EXCHANGE_REAL_CLASS.getSimpleName()));
			final IAlgorithm algorithm = (IAlgorithm) IConfiguration.ALGORITHM_CLASS.getConstructor().newInstance();

			logger.info(String.format("Constructing engine class: \"%s\"", IConfiguration.EXCHANGE_REAL_CLASS.getSimpleName()));
			final IEngine engine = (IEngine) IConfiguration.ENGINE_CLASS.getConstructor(IExchange.class, IAlgorithm.class).newInstance(exchange, algorithm);

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
