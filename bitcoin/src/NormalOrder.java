

public class NormalOrder extends Order{

	private double price;

	//TODO add currency

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "NormalOrder [price=" + price + "]" + super.toString();
	}

}
