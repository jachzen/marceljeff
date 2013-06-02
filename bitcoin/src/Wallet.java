/**
 * Represents the current state of the account TODO for the time being we do not
 * support currencies (everything is in euro?)
 *
 * @author mpater
 */
public class Wallet {
    private double balance;
    private double bitCoins;

    public Wallet(double balance) {
        this.balance = balance;
        this.bitCoins = 0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBitCoins() {
        return bitCoins;
    }

    public void setBitCoins(double bitCoins) {
        this.bitCoins = bitCoins;
    }

    public double deposit(double amount) {
        balance += amount;
        return balance;
    }

    public double withdraw(double amount) {
        balance -= amount;
        return balance;
    }

	public double getBalanceBitcoins(double exchangeRate) {
		return exchangeRate * balance;
	}

    @Override
    public String toString() {
        return "{balance=" + balance + "}";
    }
}
