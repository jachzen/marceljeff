package to.sparks.mtgox.model;

import java.math.BigDecimal;

/**
 * A representation of bitcoins as used with MtGox
 *
 * @author SparksG
 */
public class MtGoxBitcoin extends MtGoxUnitOfCredit {

    /*
     * A convenience method for creating Bitcoin units of currency. Not
     * recommended though because you shouldn't really be storing monetary
     * values as doubles anyway. Use BigDecimal instead.
     */
    public MtGoxBitcoin(double float_value) {
        super(float_value, CurrencyInfo.BitcoinCurrencyInfo);
    }

    /*
     * Create bitcoin units of currency This is an attempt to be directly
     * compatible with the MtGox API price_int parameter, which has variable
     * scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxBitcoin(long int_value) {
        super(int_value, CurrencyInfo.BitcoinCurrencyInfo);
    }

    public MtGoxBitcoin(BigDecimal value) {
        super(value, CurrencyInfo.BitcoinCurrencyInfo);
    }
}
