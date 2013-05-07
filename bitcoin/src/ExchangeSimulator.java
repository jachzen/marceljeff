

/**
 * Analysis of mtgox:
 *  - marketorders (buy/sell at marketprice) execute immediately, therefore we do a very simple simulator in the first step
 *  - needs more analysis to make other orders (perhaps add to ordertype(marketorder/normalorder))
 * 
 * @author mpater
 *
 */
public class ExchangeSimulator implements IExchange {

	private Wallet wallet = new Wallet(IConfiguration.BALANCE);

    @Override
    public double getCurrentPrice() {
        return 0;
    }

    @Override
	public Order placeBuyOrder(NormalOrder order) {
		//immediate transaction
		wallet.withdraw(order.getPrice() + (IConfiguration.FEE_PERCENTAGE * order.getPrice()));
		order.setState(Order.State.DONE);
		return order;
    }

    @Override
	public Order placeSellOrder(NormalOrder order) {
		//immediate transaction
		wallet.deposit(order.getPrice());
		order.setState(Order.State.DONE);
		return order;
    }

    @Override
    public Order getCurrentOrder() {
        return null;
    }

    @Override
    public void cancelOrder(Order order) {
    }

	@Override
	public Wallet getWallet() {
		return wallet;
	}

	@Override
	public Order placeBuyOrder(MarketOrder order) {
		//immediate transaction
		wallet.withdraw(getCurrentPrice() + (IConfiguration.FEE_PERCENTAGE * getCurrentPrice()));
		order.setState(Order.State.DONE);
		return order;
	}

	@Override
	public Order placeSellOrder(MarketOrder order) {
		//immediate transaction
		wallet.deposit(getCurrentPrice());
		order.setState(Order.State.DONE);
		return order;
	}
}
