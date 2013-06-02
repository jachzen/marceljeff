import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.AccountInfo;
import to.sparks.mtgox.model.CurrencyInfo;
import to.sparks.mtgox.model.Lag;
import to.sparks.mtgox.model.MtGoxBitcoin;
import to.sparks.mtgox.model.MtGoxFiatCurrency;
import to.sparks.mtgox.model.OrderResult;

public class ExchangeMtGox implements IExchange {

	private MtGoxHTTPClient mtgoxEUR;
	private CurrencyInfo currencyInfo;

	private final Logger logger = Logger.getLogger(ExchangeMtGox.class.getName());

	public ExchangeMtGox() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
		mtgoxEUR = (MtGoxHTTPClient) context.getBean("mtgoxEUR");
		Lag lag;
		try {
			lag = mtgoxEUR.getLag();
			currencyInfo = mtgoxEUR.getCurrencyInfo(mtgoxEUR.getBaseCurrency());

			logger.info("Current lag: {0}" + lag.getLag());
			// Get the private account info
			AccountInfo info = mtgoxEUR.getAccountInfo();
			logger.info("Logged into account: {0}" + info.getLogin());
		} catch (Exception e) {
			logger.error("GetOpenOrders failed!", e);
			return;
		}
	}

    @Override
	public Order getCurrentOrder() {
		to.sparks.mtgox.model.Order[] openOrders;
		try {
			openOrders = mtgoxEUR.getOpenOrders();
		} catch (Exception e) {
			logger.error("GetOpenOrders failed!", e);
			return null;
		}
		List<Order> orders = new ArrayList<Order>(openOrders.length);
		for (to.sparks.mtgox.model.Order order : openOrders) {
			NormalOrder ourOrder = new NormalOrder();
			ourOrder.setOrderType((order.getType() == MtGoxHTTPClient.OrderType.Bid) ? OrderType.BUY : OrderType.SELL);
			ourOrder.setPrice(order.getPrice().getPriceValueInt());
			orders.add(ourOrder);
			//TODO what are the return values of getStatus???
			//ourOrder.setState(order.getStatus());
		}
		assert (orders.size() <= 1);
		return (orders.size() == 0) ? new NoOrder() : orders.get(0);
    }

    @Override
	public boolean cancelOrder(Order order) {
		try {
			mtgoxEUR.cancelOrder((order.getOrderType() == OrderType.BUY) ? to.sparks.mtgox.MtGoxHTTPClient.OrderType.Bid : to.sparks.mtgox.MtGoxHTTPClient.OrderType.Ask, order.getReference());
		} catch (Exception e) {
			return false;
		}
		return true;
    }

	@Override
	public Wallet getWallet() {
		try {
			@SuppressWarnings("unused")
			HashMap<String, to.sparks.mtgox.model.Wallet> wallets = mtgoxEUR.getAccountInfo().getWallets();

			to.sparks.mtgox.model.Wallet theirWallet = wallets.get("mtgoxEUR");
			//TODO get amounts of bitcoins from somewhere...
			Wallet ourWallet = new Wallet(theirWallet.getBalance().doubleValue());
			//ourWallet.setBitCoins( mtgoxEUR.getAccountInfo();
		}catch (Exception e) {
			logger.error("GetWallets failed", e);
			return null;
		}

		return null;
	}

	@Override
	public Order placeBuyOrder(NormalOrder order) {
		MtGoxBitcoin bitcoins = new MtGoxBitcoin(order.getVolume());
		MtGoxFiatCurrency price = new MtGoxFiatCurrency(order.getPrice(), currencyInfo);
		String reference;
		try {
			reference = mtgoxEUR.placeOrder(to.sparks.mtgox.MtGoxHTTPClient.OrderType.Bid, price, bitcoins);
			order.setReference(reference);
		} catch (Exception e) {
			logger.error("Place Order", e);
			return null;
		}
		return order;
	}

	@Override
	public Order placeSellOrder(NormalOrder order) {
		MtGoxBitcoin bitcoins = new MtGoxBitcoin(order.getVolume());
		MtGoxFiatCurrency price = new MtGoxFiatCurrency(order.getPrice(), currencyInfo);
		String reference;
		try {
			reference = mtgoxEUR.placeOrder(to.sparks.mtgox.MtGoxHTTPClient.OrderType.Ask, price, bitcoins);
			order.setReference(reference);
		} catch (Exception e) {
			logger.error("Place Order", e);
			return null;
		}
		return order;
	}

	@Override
	public Order placeBuyOrder(MarketOrder order) {
		MtGoxBitcoin bitcoins = new MtGoxBitcoin(order.getVolume());
		String reference;
		try {
			reference = mtgoxEUR.placeMarketOrder(to.sparks.mtgox.MtGoxHTTPClient.OrderType.Bid, bitcoins);
			order.setReference(reference);
		} catch (Exception e) {
			logger.error("Place Order", e);
			return null;
		}
		return order;
	}

	@Override
	public Order placeSellOrder(MarketOrder order) {
		MtGoxBitcoin bitcoins = new MtGoxBitcoin(order.getVolume());
		String reference;
		try {
			reference = mtgoxEUR.placeMarketOrder(to.sparks.mtgox.MtGoxHTTPClient.OrderType.Ask, bitcoins);
			order.setReference(reference);
		} catch (Exception e) {
			logger.error("Place Order", e);
			return null;
		}

		return order;
	}

	@Override
	public Ticker getTicker() {
		try {
			to.sparks.mtgox.model.Ticker mtGoxTicker = mtgoxEUR.getTicker();
			Ticker ticker = new Ticker(new Date(), mtGoxTicker.getAvg().doubleValue());
			return ticker;
		} catch (Exception e) {
			// Do s.th. more effective?!
		}
		return null;
	}

	@Override
	public Order.State getOrderResult(NormalOrder order) {
		to.sparks.mtgox.MtGoxHTTPClient.OrderType orderTypeMtGox = (order.getOrderType() == OrderType.BUY)? (to.sparks.mtgox.MtGoxHTTPClient.OrderType.Bid) : (to.sparks.mtgox.MtGoxHTTPClient.OrderType.Ask);
		OrderResult result;
		try {
			result = mtgoxEUR.getOrderResult(orderTypeMtGox, order.getReference());
		} catch (Exception e) {
			return Order.State.OPEN;
		}
		if (result.getTotalAmount().getPriceValueInt() == order.getVolume() * order.getPrice())
			return Order.State.DONE;
		else
			return Order.State.PENDING;
	}

}
