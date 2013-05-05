package to.sparks.mtgox.model;

import java.math.BigDecimal;

/**
 * A representation of litecoins as used with MtGox
 *
 * @author SparksG
 */
public class MtGoxLitecoin extends MtGoxUnitOfCredit {

    public MtGoxLitecoin(double float_value) {
        super(float_value, CurrencyInfo.LitecoinCurrencyInfo);
    }

    public MtGoxLitecoin(long int_value) {
        super(int_value, CurrencyInfo.LitecoinCurrencyInfo);
    }

    public MtGoxLitecoin(BigDecimal value) {
        super(value, CurrencyInfo.LitecoinCurrencyInfo);
    }
}
