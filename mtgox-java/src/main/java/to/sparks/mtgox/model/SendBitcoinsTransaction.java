package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class SendBitcoinsTransaction extends DtoBase {

    private String trx;

    public SendBitcoinsTransaction(@JsonProperty("trx") String trx) {
        this.trx = trx;
    }

    public String getTrx() {
        return trx;
    }
}
