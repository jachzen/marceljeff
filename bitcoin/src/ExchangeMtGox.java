public class ExchangeMtGox implements IExchange {

    @Override
    public double getCurrentPrice() {
        return 0;
    }

    @Override
    public Order placeBuyOrder() {
        return null;
    }

    @Override
    public Order placeSellOrder() {
        return null;
    }

    @Override
    public Order getCurrentOrder() {
        return null;
    }

    @Override
    public void cancelOrder(Order order) {
    }
}
