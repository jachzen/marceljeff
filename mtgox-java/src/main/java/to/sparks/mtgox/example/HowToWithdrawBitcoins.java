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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.MtGoxBitcoin;
import to.sparks.mtgox.model.SendBitcoinsTransaction;
import to.sparks.mtgox.model.Wallet;

/**
 * Example that shows how to transfer the entire bitcoin balance of your MtGox
 * account to a bitcoin address given on the command line.
 * OTP is not supported! Please turn off Yubikey/OTP
 *
 * @author SparksG
 */
public class HowToWithdrawBitcoins {

    static final Logger logger = Logger.getLogger(HowToWithdrawBitcoins.class.getName());

    /** *
     * Send the entire bitcoin balance of a MtGox account to a destination
     * bitcoin address.
     * OTP is not supported! Please turn off Yubikey/OTP
     *
     * @param args The destination bitcoin address
     * @throws Exception OTP is not supported! Please turn off Yubikey/OTP
     */
    public static void main(String[] args) throws Exception {

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
        MtGoxHTTPClient mtGoxAPI = (MtGoxHTTPClient) context.getBean("mtgoxUSD");

        HashMap<String, Wallet> wallets = mtGoxAPI.getAccountInfo().getWallets();
        Wallet btcWallet = wallets.get("BTC");
        MtGoxBitcoin mtgoxBalance = (MtGoxBitcoin) btcWallet.getBalance();
        logger.log(Level.INFO, "MtGox account balance: BTC {0}", mtgoxBalance.toPlainString());
        if (mtgoxBalance.compareTo(BigDecimal.ZERO) > 0) {

            MtGoxBitcoin fee = new MtGoxBitcoin(0.0005D); // Transaction fee
            MtGoxBitcoin transferAmount = new MtGoxBitcoin(mtgoxBalance.subtract(fee));

            if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                logger.log(Level.INFO, "Transferring BTC {0} to bitcoin address {1} and paying fee {2}",
                        new Object[]{
                            transferAmount.toPlainString(),
                            args[0],
                            fee.toPlainString()
                        });
                SendBitcoinsTransaction trx = mtGoxAPI.sendBitcoins(args[0], transferAmount, fee, true, false);
                logger.log(Level.INFO, "Transfer success.  trx: {0}", trx.getTrx());
            }
        }
    }
}
