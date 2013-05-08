public class ExchangeMtGox implements IExchange {

    @Override
    public Order getCurrentOrder() {
        return null;
    }

    @Override
    public void cancelOrder(Order order) {
    }

	@Override
	public Wallet getWallet() {
		return null;
	}

	@Override
	public Order placeBuyOrder(NormalOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order placeSellOrder(NormalOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order placeBuyOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order placeSellOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction getTicker() {
		// TODO Auto-generated method stub
		return null;
	}

}
