package to.sparks.mtgox.model;

import java.math.BigDecimal;

/**
 * A representation of namecoins as used with MtGox
 *
 * @author SparksG
 */
public class MtGoxNamecoin extends MtGoxUnitOfCredit {

    public MtGoxNamecoin(double float_value) {
        super(float_value, CurrencyInfo.NamecoinCurrencyInfo);
    }

    public MtGoxNamecoin(long int_value) {
        super(int_value, CurrencyInfo.NamecoinCurrencyInfo);
    }

    public MtGoxNamecoin(BigDecimal value) {
        super(value, CurrencyInfo.NamecoinCurrencyInfo);
    }
}
