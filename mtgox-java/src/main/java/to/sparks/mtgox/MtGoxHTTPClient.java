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
package to.sparks.mtgox;

import java.util.Currency;
import to.sparks.mtgox.model.*;

/**
 * A java api for the MtGox bitcoin exchange built using Spring and Maven. See
 * https://en.bitcoin.it/wiki/MtGox/API for protocol details.
 *
 * @author SparksG
 */
public interface MtGoxHTTPClient {

    public enum OrderType {

        Bid, Ask
    }

    /**
     *
     * @return The java currency used by this api connection.
     */
    Currency getBaseCurrency();

    /**
     * Get the market full depth from the MtGox exchange
     *
     * @return An object containing the full market depth information
     * @throws Exception
     */
    FullDepth getFullDepth() throws Exception;

    /**
     * A private function that requires credentials. Get all open orders.
     *
     * @return An array of open orders
     * @throws Exception
     */
    Order[] getOpenOrders() throws Exception;

    /**
     * Return the result of an order request
     *
     * @param orderType Bid or Ask
     * @param orderRef  The reference string that was returned when the order
     * was
     * pleaced
     * @return
     * @throws Exception
     */
    OrderResult getOrderResult(OrderType orderType, String orderRef) throws Exception;

    /**
     * Request the current ticker from MtGox
     *
     * @return
     * @throws Exception
     */
    Ticker getTicker() throws Exception;

    /**
     * Place a bitcoin order at market price.
     *
     * @param orderType Bid or Ask
     * @param volume    The volume of bitcoins
     * @return Order reference number
     * @throws Exception
     */
    String placeMarketOrder(OrderType orderType, MtGoxBitcoin volume) throws Exception;

    /**
     * Place a bitcoin order at a particular price
     *
     * @param orderType Bid or Ask
     * @param price     The price in the base currency of this api instance
     * @param volume    The volume of bitcoins
     * @return Order reference number
     * @throws Exception
     */
    String placeOrder(OrderType orderType, MtGoxFiatCurrency price, MtGoxBitcoin volume) throws Exception;

    /**
     * Get private information about the currently logged-in account
     *
     * @return
     * @throws Exception
     */
    AccountInfo getAccountInfo() throws Exception;

    /**
     * Get MtGox special information about a currency.
     *
     * @param currency The java currency
     * @return
     * @throws Exception
     */
    CurrencyInfo getCurrencyInfo(Currency currency) throws Exception;

    /**
     * Get MtGox special information about a currency.
     *
     * @param currencyCode The currency code
     * @return
     * @throws Exception
     */
    CurrencyInfo getCurrencyInfo(String currencyCode) throws Exception;

    /**
     * Cancel an existing order. Currently this is only available on the HTTP
     * Version 0 API, so the JSON result should be discarded.
     *
     * @param orderType Bid or Ask
     * @param orderRef  The reference returned when the order was placed
     * @return The JSON response. Avoid using this response because this api
     * call uses the old Version 0 MtGox API.
     * @throws Exception
     */
    OrderCancelResult cancelOrder(OrderType orderType, String orderRef) throws Exception;

    OrderCancelResult cancelOrder(Order order) throws Exception;

    /**
     * Send bitcoins from your MtGox account balance to a bitcoin address.
     *
     * @param destinationAddress The destination bitcoin address that should
     * receive the funds
     * @param bitcoins           The amount of bitcoins to transfer from your
     * MtGox account
     * @param fee                The fee you would like to pay the network
     * @param isNoInstant        If true, use the bitcoin blockchain, even if
     * the receiving address is on MtGox
     * @param isGreen            If true, use a greenaddress
     * https://en.bitcoin.it/wiki/GreenAddress
     * @return
     */
    SendBitcoinsTransaction sendBitcoins(String destinationAddress, MtGoxBitcoin bitcoins, MtGoxBitcoin fee, boolean isNoInstant, boolean isGreen) throws Exception;

    /**
     * The "lag" value is the age in microseconds of the oldest order pending
     * execution If it's too large it means the engine is busy, and the depth is
     * probably not reliable.
     *
     * @return The order lag in milliseconds
     * @throws Exception
     */
    Lag getLag() throws Exception;

    /**
     * Get only the trades since a given trade id, use the parameter sinceTid
     *
     * @param sinceTid
     * @return
     * @throws Exception
     */
    Trade[] getTradesSince(String sinceTid) throws Exception;
}
