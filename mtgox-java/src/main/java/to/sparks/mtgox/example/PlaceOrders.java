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
package to.sparks.mtgox.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.CurrencyInfo;
import to.sparks.mtgox.model.MtGoxBitcoin;
import to.sparks.mtgox.model.MtGoxFiatCurrency;

/**
 * Examples of ordering functionality.
 * WARNING: Executing this code will attempt to place orders at the MtGox
 * Bitcoin Exchange
 *
 * @author SparksG
 */
public class PlaceOrders {

    static final Logger logger = Logger.getLogger(PlaceOrders.class.getName());

    public static void main(String[] args) throws Exception {

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
        MtGoxHTTPClient mtgoxUSD = (MtGoxHTTPClient) context.getBean("mtgoxUSD");

        // Obtain information about the base currency of the API instance
        CurrencyInfo currencyInfo = mtgoxUSD.getCurrencyInfo(mtgoxUSD.getBaseCurrency());
        logger.log(Level.INFO, "Base currency: {0}", currencyInfo.getCurrency().getCurrencyCode());


        // Purchase 1.0 bitcoins for USD$0.01
        MtGoxFiatCurrency fiatUnit = new MtGoxFiatCurrency(0.01D, currencyInfo);  // We use the currencyInfo to be explicit about what this money represents
        MtGoxBitcoin bitcoinUnit = new MtGoxBitcoin(1.0D);  // You should probably use BigDecimals instead of double types to represent money.
        String orderRef = mtgoxUSD.placeOrder(MtGoxHTTPClient.OrderType.Bid, fiatUnit, bitcoinUnit);
        logger.log(Level.INFO, "orderRef: {0}", new Object[]{orderRef});

        // Cancel the order
        mtgoxUSD.cancelOrder(MtGoxHTTPClient.OrderType.Bid, orderRef);
    }
}
