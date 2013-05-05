package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class AccountInfo extends DtoBase {

    private String login;
    private String[] rights;
    private Date last_Login;
    private Date created;
    private long index;
    private BitcoinPrice monthly_Volume;
    private String language;
    private String id;
    private HashMap<String, Wallet> wallets;
    private double trade_Fee;

    public AccountInfo(@JsonProperty("Login") String login,
            @JsonProperty("Rights") String[] rights,
            @JsonProperty("Last_Login") Date last_Login,
            @JsonProperty("Created") Date created,
            @JsonProperty("Index") long index,
            @JsonProperty("Monthly_Volume") BitcoinPrice monthly_Volume,
            @JsonProperty("Language") String language,
            @JsonProperty("Id") String id,
            @JsonProperty("Wallets") HashMap<String, Wallet> wallets,
            @JsonProperty("Trade_Fee") double trade_Fee) {
        this.login = login;
        this.rights = rights;
        this.last_Login = last_Login;
        this.created = created;
        this.index = index;
        this.monthly_Volume = monthly_Volume;
        this.language = language;
        this.id = id;
        this.wallets = wallets;
        this.trade_Fee = trade_Fee;
    }

    public String getLogin() {
        return login;
    }

    public String[] getRights() {
        return rights;
    }

    public Date getLast_Login() {
        return last_Login;
    }

    public Date getCreated() {
        return created;
    }

    public long getIndex() {
        return index;
    }

    public BitcoinPrice getMonthly_Volume() {
        return monthly_Volume;
    }

    public String getLanguage() {
        return language;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Wallet> getWallets() {
        return wallets;
    }

    public double getTrade_Fee() {
        return trade_Fee;
    }
}
