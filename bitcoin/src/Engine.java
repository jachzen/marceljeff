import java.io.IOException;
import java.util.ArrayList;

public class Engine implements IEngine {
    // private final Logger logger = Logger.getLogger(Engine.class.getName());
    private final BitCoinGenome genome;
    private final IExchange exchange;
	private Order lastExecutedOrder;
	private Order pendingOrder;
	//private Wallet engineWallet = new Wallet(IConfiguration.WALLET_BALANCE);
	private OrderState state = OrderState.INIT;
	
	private enum OrderState {
		DOBUY, BOUGHT, DOSELL, SOLD, INIT
	}

    public Engine(IExchange exchange, BitCoinGenome algorithm) {
        this.genome = algorithm;
        this.exchange = exchange;
		this.pendingOrder = new NoOrder();
        genome.resetProcessing(exchange.getTicker());
    }

    @Override
    public void run(ArrayList<Double> debugData) {
        Ticker lastTicker = exchange.getTicker();
        OrderType intend;

        try {
            intend = genome.addTicker(lastTicker, debugData);
        } catch (IOException e) {
            return;
        }

		NormalOrder order = new NormalOrder();
		Wallet wallet = exchange.getWallet();
		
        // state machine needed for not immediate executed tickers
		switch (state) {
		
		case DOBUY:
            switch (intend) {
			case DO_NOTHING:
            case BUY:
				//TODO what if cancelling is not working?

				if (exchange.getOrderResult((NormalOrder) pendingOrder) == Order.State.DONE || wallet.getBalance() == 0) {
					state = OrderState.BOUGHT;
					break;
				}

				pendingOrder = (exchange.cancelOrder(pendingOrder) == true) ? new NoOrder() : pendingOrder;
				order.setPrice(lastTicker.getPrice());
				order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
				order.setOrderType(OrderType.BUY);
				order.setState(Order.State.OPEN);
				pendingOrder = exchange.placeBuyOrder(order);
				state = OrderState.DOBUY;
                break;
            case SELL:
				//TODO what if cancelling is not working?
				pendingOrder = (exchange.cancelOrder(pendingOrder) == true) ? new NoOrder() : pendingOrder;
				if (exchange.getWallet().getBitCoins() != 0) {
					order = new NormalOrder();
					order.setPrice(lastTicker.getPrice());
					order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
					order.setOrderType(OrderType.SELL);
					order.setState(Order.State.OPEN);
					pendingOrder = exchange.placeSellOrder(new NormalOrder());
					state = OrderState.DOSELL;
				} else {
					state = OrderState.SOLD;
				}
				break;
			}
            break;
		case BOUGHT:
			switch (intend) {
			case BUY:
				state = OrderState.BOUGHT;
				break;
			case SELL:
				if (exchange.getWallet().getBitCoins() != 0) {
					order = new NormalOrder();
					order.setPrice(lastTicker.getPrice());
					order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
					order.setOrderType(OrderType.SELL);
					order.setState(Order.State.OPEN);
					pendingOrder = exchange.placeSellOrder(new NormalOrder());
					state = OrderState.DOSELL;
				} else {
					state = OrderState.SOLD;
				}
				break;

			case DO_NOTHING:
				state = OrderState.BOUGHT;
				break;
			}
			break;
		case DOSELL:
            switch (intend) {
			case DO_NOTHING:
            case BUY:
				//TODO what if cancelling is not working?
				pendingOrder = (exchange.cancelOrder(pendingOrder) == true) ? new NoOrder() : pendingOrder;
				if (exchange.getWallet().getBalance() != 0) {
					order.setPrice(lastTicker.getPrice());
					order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
					order.setOrderType(OrderType.BUY);
					order.setState(Order.State.OPEN);
					pendingOrder = exchange.placeBuyOrder(order);
					state = OrderState.DOBUY;
				} else {
					state = OrderState.BOUGHT;
				}
                break;
            case SELL:
				if (exchange.getOrderResult((NormalOrder) pendingOrder) == Order.State.DONE || wallet.getBitCoins() == 0) {
					state = OrderState.SOLD;
					break;
				}
				//TODO what if cancelling is not working?
				pendingOrder = (exchange.cancelOrder(pendingOrder) == true) ? new NoOrder() : pendingOrder;
				order = new NormalOrder();
				order.setPrice(lastTicker.getPrice());
				order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
				order.setOrderType(OrderType.SELL);
				order.setState(Order.State.OPEN);
				pendingOrder = exchange.placeSellOrder(new NormalOrder());
				state = OrderState.DOSELL;
				break;
			}
		case SOLD:
			switch (intend) {
			case SELL:
				state = OrderState.SOLD;
				break;
			case BUY:
				if (exchange.getWallet().getBalance() != 0) {
					order = new NormalOrder();
					order.setPrice(lastTicker.getPrice());
					order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
					order.setOrderType(OrderType.BUY);
					order.setState(Order.State.OPEN);
					pendingOrder = exchange.placeBuyOrder(new NormalOrder());
					state = OrderState.DOBUY;
				} else {
					state = OrderState.BOUGHT;
				}
				break;

			case DO_NOTHING:
				state = OrderState.BOUGHT;
				break;
			}
		case INIT:
            switch (intend) {
			case DO_NOTHING:
				state = OrderState.INIT;
				break;
			case BUY:
				pendingOrder = (exchange.cancelOrder(pendingOrder) == true) ? new NoOrder() : pendingOrder;
				order.setPrice(lastTicker.getPrice());
				order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
				order.setOrderType(OrderType.BUY);
				order.setState(Order.State.OPEN);
				pendingOrder = exchange.placeBuyOrder(order);
				state = OrderState.DOBUY;
				break;
			case SELL:
				if (exchange.getWallet().getBitCoins() != 0) {
					order = new NormalOrder();
					order.setPrice(lastTicker.getPrice());
					order.setVolume(exchange.getWallet().getBalanceBitcoins(lastTicker.getPrice()));
					order.setOrderType(OrderType.SELL);
					order.setState(Order.State.OPEN);
					pendingOrder = exchange.placeSellOrder(new NormalOrder());
					state = OrderState.DOSELL;
				} else {
					state = OrderState.SOLD;
				}
				break;
            }

            break;
        default:
            break;
        }
        // logger.debug(exchange.getWallet().toString());
    }
}
