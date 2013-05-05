/*
 * The MtGox-Java API is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The MtGox-Java API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with the MtGox-Java API .  If not, see <http://www.gnu.org/licenses/>.
 */
package to.sparks.mtgox.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Currency;
import java.util.HashMap;
import java.util.logging.Logger;

import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.AccountInfo;
import to.sparks.mtgox.model.CurrencyInfo;
import to.sparks.mtgox.model.CurrencyKludge;
import to.sparks.mtgox.model.DtoBase;
import to.sparks.mtgox.model.FullDepth;
import to.sparks.mtgox.model.Lag;
import to.sparks.mtgox.model.MtGoxBitcoin;
import to.sparks.mtgox.model.MtGoxFiatCurrency;
import to.sparks.mtgox.model.Order;
import to.sparks.mtgox.model.OrderCancelResult;
import to.sparks.mtgox.model.OrderResult;
import to.sparks.mtgox.model.SendBitcoinsTransaction;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.model.Trade;

/**
 * All MtGox API interactions (both HTTP/REST and Websocket) are handled by this
 * API class.
 *
 * @author SparksG
 */
class APIService implements MtGoxHTTPClient {

    private static Logger logger;
    HTTPClientV0Service httpAPIV0;
    HTTPClientV1Service httpAPIV1;
    CurrencyInfo currencyInfo;

    public APIService(final Logger logger, HTTPClientV0Service httpAPIV0, HTTPClientV1Service httpAPIV1, Currency currency) throws IOException, Exception {
        this.logger = logger;
        this.httpAPIV0 = httpAPIV0;
        this.httpAPIV1 = httpAPIV1;
        this.currencyInfo = httpAPIV1.getCurrencyInfo(currency);
    }

    @Override
    public FullDepth getFullDepth() throws IOException, Exception {
        FullDepth fullDepth = httpAPIV1.getFullDepth(currencyInfo.getCurrency());
        currencyKludge(fullDepth);
        return fullDepth;
    }

    @Override
    public String placeOrder(OrderType orderType, MtGoxFiatCurrency price, MtGoxBitcoin volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

		HashMap<String, String> params = new HashMap<String, String>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }

        if (price != null) {
            params.put("price_int", String.valueOf(price.unscaledValue().longValue()));
        }
        params.put("amount_int", String.valueOf(volume.unscaledValue().longValue()));

        return httpAPIV1.placeOrder(currencyInfo.getCurrency(), params);
    }

    @Override
    public String placeMarketOrder(OrderType orderType, MtGoxBitcoin volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        return placeOrder(orderType, null, volume);
    }

    @Override
    public OrderResult getOrderResult(OrderType orderType, String orderRef) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
		HashMap<String, String> params = new HashMap<String, String>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        params.put("order", orderRef);

        OrderResult orderResult = httpAPIV1.getPrivateOrderResult(params);
        currencyKludge(orderResult);

        return orderResult;
    }

    @Override
    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        Order[] orders = httpAPIV1.getOpenOrders();
        for (Order order : orders) {
            currencyKludge(order);
        }
        return orders;
    }

    @Override
    public Ticker getTicker() throws IOException, Exception {
        Ticker ticker = httpAPIV1.getTicker(currencyInfo.getCurrency());
        currencyKludge(ticker);
        return ticker;
    }

    @Override
    public Currency getBaseCurrency() {
        return currencyInfo.getCurrency();
    }

    @Override
    public AccountInfo getAccountInfo() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        AccountInfo info = httpAPIV1.getPrivateInfo();
        currencyKludge(info);
        for (String key : info.getWallets().keySet()) {
            CurrencyInfo walletCurrencyInfo = getCurrencyInfo(key);
            ((CurrencyKludge) info.getWallets().get(key)).setCurrencyInfo(walletCurrencyInfo);
        }
        return info;
    }

    @Override
    public CurrencyInfo getCurrencyInfo(Currency currency) throws IOException, Exception {
        return httpAPIV1.getCurrencyInfo(currency);
    }

    @Override
    public CurrencyInfo getCurrencyInfo(String currencyCode) throws Exception {
        return httpAPIV1.getCurrencyInfo(currencyCode);
    }

    private void currencyKludge(DtoBase o) {
        if (o != null && o instanceof CurrencyKludge) {
            ((CurrencyKludge) o).setCurrencyInfo(currencyInfo);
        }
    }

    @Override
    public OrderCancelResult cancelOrder(OrderType orderType, String orderRef) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
        if (orderType == MtGoxHTTPClient.OrderType.Bid) {
            params.put("type", "2");
        } else {
            params.put("type", "1");
        }
        params.put("oid", orderRef);
        return httpAPIV0.cancelOrder(params);
    }

    @Override
    public OrderCancelResult cancelOrder(Order order) throws Exception {
        return cancelOrder(order.getType(), order.getOid());
    }

    @Override
    public SendBitcoinsTransaction sendBitcoins(String destinationAddress, MtGoxBitcoin bitcoins, MtGoxBitcoin fee, boolean isNoInstant, boolean isGreen) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("address", destinationAddress);
        params.put("amount_int", String.valueOf(bitcoins.unscaledValue().longValue()));
        params.put("fee_int", String.valueOf(fee.unscaledValue().longValue()));
        params.put("no_instant", isNoInstant ? "1" : "0");
        params.put("green", isGreen ? "1" : "0");
        return httpAPIV1.sendBitcoins(params);
    }

    @Override
    public Lag getLag() throws Exception {
         
         return httpAPIV1.getLag();
    }
    
	@Override
	public Trade[] getTradesSince(String sinceTid) throws IOException, Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("since",sinceTid);
		return httpAPIV1.getTradesSince(currencyInfo.getCurrency(), params);
	}
}