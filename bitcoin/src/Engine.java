import java.io.IOException;

import org.apache.log4j.Logger;

public class Engine implements IEngine {
    private final Logger logger = Logger.getLogger(Engine.class.getName());
	private IAlgorithm algorithm;
	private IExchange exchange;

	public Engine(IExchange exchange, IAlgorithm algorithm) {
		this.algorithm = algorithm;
		this.exchange = exchange;
		algorithm.setInitialTransaction(exchange.getTicker());
	}

	@Override
	public void run() {
		Ticker lastTicker = exchange.getTicker();
		OrderType intend;

		try {
			intend = algorithm.addTransaction(lastTicker);
		} catch (IOException e) {
			return;
		}

		Order currentOrder = exchange.getCurrentOrder();

		// state machine needed for not immediate executed transactions
		switch (currentOrder.getOrderType()) {
		case BUY:
			switch (intend) {
			case BUY:
				// TODO Marcel: Ich dachte, in diesem Fall eher
				// "Do nothing".
				//currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
				//currentOrder = exchange.placeBuyOrder(new NormalOrder());
				break;
			case SELL:
				currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
				currentOrder = exchange.placeSellOrder(new NormalOrder());
				break;
			case DO_NOTHING:
				currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
				break;
			}
			break;
		case DO_NOTHING:
			switch (intend) {
			case BUY:
				currentOrder = exchange.placeBuyOrder(new NormalOrder());
				break;
			case SELL:
				currentOrder = exchange.placeSellOrder(new NormalOrder());
				break;
			case DO_NOTHING:
				break;
			}
			break;
		case SELL:
			switch (intend) {
			case BUY:
				currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
				currentOrder = exchange.placeBuyOrder(new NormalOrder());
				break;
			case SELL:
				// TODO Marcel: Ich dachte, in diesem Fall eher
				// "Do nothing".
				//currentOrder = exchange.cancelOrder(currentOrder);
				//currentOrder = exchange.placeSellOrder(new NormalOrder());
				break;
			case DO_NOTHING:
				currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
				break;
			}
			break;
		default:
			break;
		}
		//logger.debug(exchange.getWallet().toString());
	}
}
