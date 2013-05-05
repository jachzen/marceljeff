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

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * TODO: This should be a Spring service API.
 *
 * @author SparksG
 */
class UrlFactory {

    public enum Command {

        PrivateOrderAdd,
        PrivateOrderResult,
        PrivateOrders,
        PrivateInfo,
        FullDepth,
        Ticker,
        CurrencyInfo,
        PrivateOrderCancel,
        SendBitcoins,
        Lag,
        Trades
    }
    private static String MTGOX_HTTP_API_URL = "https://mtgox.com/api/";
    private static String MTGOX_HTTP_API_CACHE_URL = "https://data.mtgox.com/api/";
    private static String MTGOX_HTTP_API_VERSION_0 = "0/";
    private static String MTGOX_HTTP_API_VERSION_1 = "1/";
    private static final HashMap<Currency, String> currencyMap;
    private static final HashMap<Command, String> apiV0RestMap;
    private static final HashMap<Command, String> apiV1RestMap;
    private static final List<Command> readOnlyCommands;
    private static final String[] currencyList = {"USD", "AUD", "CAD", "CHF", "CNY", "DKK", "EUR", "GBP", "HKD", "JPY", "NZD", "PLN", "RUB", "SEK", "SGD", "THB"};

    static {
		currencyMap = new HashMap<Currency, String>();
        for (String currency : currencyList) {
            currencyMap.put(Currency.getInstance(currency), "BTC" + currency + "/");
        }

        // These commands are ok to get from the cache api
		readOnlyCommands = new ArrayList<Command>();
        readOnlyCommands.add(Command.Ticker);
        readOnlyCommands.add(Command.FullDepth);
        readOnlyCommands.add(Command.Lag);
        readOnlyCommands.add(Command.CurrencyInfo);
        readOnlyCommands.add(Command.Trades);

		apiV1RestMap = new HashMap<Command, String>();
        apiV1RestMap.put(Command.PrivateOrderAdd, "private/order/add");
        apiV1RestMap.put(Command.PrivateOrderResult, "private/order/result");
        apiV1RestMap.put(Command.PrivateOrders, "private/orders");
        apiV1RestMap.put(Command.PrivateInfo, "private/info");
        apiV1RestMap.put(Command.FullDepth, "fulldepth");
        apiV1RestMap.put(Command.Ticker, "ticker");
        apiV1RestMap.put(Command.CurrencyInfo, "currency");
        apiV1RestMap.put(Command.SendBitcoins, "bitcoin/send_simple");
        apiV1RestMap.put(Command.Lag, "order/lag");
        apiV1RestMap.put(Command.Trades, "/trades");

		apiV0RestMap = new HashMap<Command, String>();
        apiV0RestMap.put(Command.PrivateOrderCancel, "cancelOrder.php");

    }

    /**
     * Only use this for 'generic' commands that don't depend on currency.
     */
    public static String getUrlForCommand(Command restCommand) throws Exception {
        return getUrlForCommand("", restCommand);
    }

    public static String getUrlForCommand(Currency currency, Command restCommand) throws Exception {
        return getUrlForCommand(currency.getCurrencyCode(), restCommand);
    }

    public static String getUrlForCommand(String currencyCode, Command restCommand) throws Exception {
        StringBuilder url = new StringBuilder();

        if (StringUtils.isEmpty(currencyCode)
                || currencyCode.equalsIgnoreCase("BTC")
                || currencyMap.containsKey(Currency.getInstance(currencyCode))) {
            if (apiV1RestMap.containsKey(restCommand)) {
                if (readOnlyCommands.contains(restCommand)) {
                    url.append(MTGOX_HTTP_API_CACHE_URL);
                } else {
                    url.append(MTGOX_HTTP_API_URL);
                }
                url.append(MTGOX_HTTP_API_VERSION_1);
                Currency currency = null;
                try {
                    currency = Currency.getInstance(currencyCode);
                } catch (IllegalArgumentException ex) {
                    // Sigh.
                }
                if (StringUtils.isEmpty(currencyCode)
                        || currencyCode.equalsIgnoreCase("BTC")
                        || !currencyMap.containsKey(currency)) {
                    url.append("generic/");
                } else {
                    url.append(currencyMap.get(Currency.getInstance(currencyCode)));
                }
                url.append(apiV1RestMap.get(restCommand));
            } else if (apiV0RestMap.containsKey(restCommand)) {
                if (readOnlyCommands.contains(restCommand)) {
                    url.append(MTGOX_HTTP_API_CACHE_URL);
                } else {
                    url.append(MTGOX_HTTP_API_URL);
                }
                url.append(MTGOX_HTTP_API_VERSION_0);
                url.append(apiV0RestMap.get(restCommand));
            } else {
                throw new Exception("Unknown command: " + restCommand.toString());
            }
        } else {
            throw new Exception("Unknown currency: " + currencyCode);
        }
        return url.toString();
    }
}
