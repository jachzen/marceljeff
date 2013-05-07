/**
 * Represents the current state of the account
 * TODO for the time being we do not support currencies (everything is in euro?)
 * @author mpater
 *
 */
public class Wallet {
	private double balance;

	public Wallet(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double deposit(double amount) {
		balance -= amount;
		return balance;
	}

	public double withdraw(double amount) {
		balance += amount;
		return balance;
	}
}
