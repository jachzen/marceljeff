public class ExchangeMtGox implements IExchange {

    @Override
    public Order getCurrentOrder() {
        return null;
    }

    @Override
    public Order cancelOrder(Order order) {
        return new NoOrder();
    }

	@Override
	public Wallet getWallet() {
		return null;
	}

	@Override
	public Order placeBuyOrder(NormalOrder order) {
		return null;
	}

	@Override
	public Order placeSellOrder(NormalOrder order) {
		return null;
	}

	@Override
	public Order placeBuyOrder(MarketOrder order) {
		return null;
	}

	@Override
	public Order placeSellOrder(MarketOrder order) {
		return null;
	}

	@Override
	public Transaction getTicker() {
		return null;
	}

}
