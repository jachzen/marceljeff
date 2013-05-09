import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    Transaction(Date date, double price, double volume) {
        date_ = date;
        price_ = price;
        volume_ = volume;
    }

    public Date getDate() {
        return date_;
    }

    public double getPrice() {
        return price_;
    }

    public double getVolume() {
        return volume_;
    }

    @Override
    public String toString() {
        return String.format("{date=%s, price=EUR%.2f, volume=%.2f}", DATE_FORMAT.format(getDate()), getPrice(),
            getVolume());
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final Date date_;
    private final double price_;
    private final double volume_;
}
