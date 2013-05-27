import java.io.IOException;

public class Engine implements IEngine {
    // private final Logger logger = Logger.getLogger(Engine.class.getName());
    private final BitCoinGenome genome;
    private final IExchange exchange;

    public Engine(IExchange exchange, BitCoinGenome algorithm) {
        this.genome = algorithm;
        this.exchange = exchange;
        genome.resetProcessing(exchange.getTicker());
    }

    @Override
    public void run() {
        Ticker lastTicker = exchange.getTicker();
        OrderType intend;

        try {
            intend = genome.addTicker(lastTicker, null);
        } catch (IOException e) {
            return;
        }

        Order currentOrder = exchange.getCurrentOrder();

        // state machine needed for not immediate executed tickers
        switch (currentOrder.getOrderType()) {
        case BUY:
            switch (intend) {
            case BUY:
                // TODO Marcel: Ich dachte, in diesem Fall eher
                // "Do nothing".
                // currentOrder = (exchange.cancelOrder(currentOrder) == true) ?
                // new NoOrder() : currentOrder;
                // currentOrder = exchange.placeBuyOrder(new NormalOrder());
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
                // currentOrder = exchange.cancelOrder(currentOrder);
                // currentOrder = exchange.placeSellOrder(new NormalOrder());
                break;
            case DO_NOTHING:
                currentOrder = (exchange.cancelOrder(currentOrder) == true) ? new NoOrder() : currentOrder;
                break;
            }
            break;
        default:
            break;
        }
        // logger.debug(exchange.getWallet().toString());
    }
}
