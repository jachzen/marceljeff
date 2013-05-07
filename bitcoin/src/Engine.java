import java.util.logging.Logger;

public class Engine implements IEngine {
	Logger logger = Logger.getLogger(Engine.class.getName());

	public void run(Database database, IExchange exchange, IAlgorithm algorithm) {
		/* TODO JEFF? ich versteh das so: korrigier mich bitte. ich weiß nicht genau, wie das funktioniert. 
		 * Du kennst dich besser mit diesem zeugs aus.  
		 * 1. wir holen die transaktionen aus der aufgezeichnetet datenbank 
		 * 2. wir geben den transaktionswert an den algorithmus, dieser sagt buy/sell/nothing
		 * 3. wir kaufen, verkaufen, etc...
		 * 
		 * Fragen: 	wie ist die fitness definiert? 
		 * 			wie lernt der algorithmus?
		 * 			wieviel kaufen wir? 
		 * 
		*/
		for (Transaction transaction : database.getTransactions()) {
			OrderType intend = algorithm.addTransaction(transaction);
			logger.info(intend.name());

			Order currentOrder = exchange.getCurrentOrder();

			if (currentOrder == null)
				continue;
			//state machine needed if no marketorders
			switch (currentOrder.getOrderType()) {
			case BUY:
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
				break;
			case DO_NOTHING:
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
				break;
			case SELL:
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
				break;
			default:
				break;
			}

		}
	}
}
