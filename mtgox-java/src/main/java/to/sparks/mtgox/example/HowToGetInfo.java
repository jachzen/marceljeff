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

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.AccountInfo;
import to.sparks.mtgox.model.Lag;
import to.sparks.mtgox.model.Order;
import to.sparks.mtgox.model.Ticker;

/**
 * This example will show how to get some types of information from MtGox
 *
 * @author SparksG
 */
public class HowToGetInfo {

    static final Logger logger = Logger.getLogger(HowToGetInfo.class.getName());

    public static void main(String[] args) throws Exception {
        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
        MtGoxHTTPClient mtgoxUSD = (MtGoxHTTPClient) context.getBean("mtgoxUSD");

        Lag lag = mtgoxUSD.getLag();
        logger.log(Level.INFO, "Current lag: {0}", lag.getLag());


        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().toPlainString());

        // Get the private account info
        AccountInfo info = mtgoxUSD.getAccountInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());

        Order[] openOrders = mtgoxUSD.getOpenOrders();

        if (ArrayUtils.isNotEmpty(openOrders)) {
            for (Order order : openOrders) {
                logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
            }
        } else {
            logger.info("There are no currently open bid or ask orders.");
        }

    }
}
