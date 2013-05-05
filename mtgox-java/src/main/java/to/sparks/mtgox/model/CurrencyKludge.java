package to.sparks.mtgox.model;

/**
 * This is a bit of a kludge that ensures the offers know what currency they are
 * in.
 *
 * @author SparksG
 */
public interface CurrencyKludge {

    void setCurrencyInfo(CurrencyInfo currencyInfo);
}
