import org.apache.log4j.Logger;

public class Engine implements IEngine {
    private final Logger logger = Logger.getLogger(Engine.class.getName());

    @Override
    public void run(IExchange exchange, IAlgorithm algorithm) {
        /* TODO Marcel: */

        /* Frage: wie ist die fitness definiert? A: Wir müssen das genau
         * definieren, aber >0 und je grösser desto besser. ZB. fangen wir mit
         * 100 Euro an und Fitness ist wieviel Euro wir am Ende der
         * Simulierungszeit haben. */

        /* Frage: wie lernt der algorithmus? A: wir simulieren 100 Algorithmen
         * mit verschiedenen Parameter, evaluieren deren Fitnesses, nehmen die
         * Top-50, erzeugen noch 50 Varianten, simulieren nochmal, usw. Wenn wir
         * so weit sind, zeig ich dir. Dann bringe ich mein
         * Optimierungsquellcode rein. */

        /* Frage: wieviel kaufen wir? Ich denke, zumindest am Anfang, so viel
         * wie möglich... oder? */

        algorithm.setInitialTransaction(exchange.getTicker());

        while (true) {
            // TODO we can poll the ticker. perhaps rename it?
            Transaction lastTicker = exchange.getTicker();
            OrderType intend = algorithm.addTransaction(lastTicker);

            Order currentOrder = exchange.getCurrentOrder();

            // state machine needed for not immediate executed transactions
            switch (currentOrder.getOrderType()) {
            case BUY:
                switch (intend) {
                case BUY:
                    // TODO Marcel: Ich dachte, in diesem Fall eher
                    // "Do nothing".
                    currentOrder = exchange.cancelOrder(currentOrder);
                    currentOrder = exchange.placeBuyOrder(new NormalOrder());
                    break;
                case SELL:
                    currentOrder = exchange.cancelOrder(currentOrder);
                    currentOrder = exchange.placeSellOrder(new NormalOrder());
                    break;
                case DO_NOTHING:
                    currentOrder = exchange.cancelOrder(currentOrder);
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
                    currentOrder = exchange.cancelOrder(currentOrder);
                    currentOrder = exchange.placeBuyOrder(new NormalOrder());
                    break;
                case SELL:
                    // TODO Marcel: Ich dachte, in diesem Fall eher
                    // "Do nothing".
                    currentOrder = exchange.cancelOrder(currentOrder);
                    currentOrder = exchange.placeSellOrder(new NormalOrder());
                    break;
                case DO_NOTHING:
                    currentOrder = exchange.cancelOrder(currentOrder);
                    break;
                }
                break;
            default:
                break;
            }
            logger.debug(exchange.getWallet().toString());
            if (!((ExchangeSimulator) exchange).increment()) {
                break;
            }
        }
    }
}
