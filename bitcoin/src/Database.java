import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Historical game database, read in from a CSV file.
 */
public class Database {

    public void addDatabaseFile(File databaseFile) throws IOException {

        CsvListReader csvReader = new CsvListReader(new FileReader(databaseFile), CsvPreference.STANDARD_PREFERENCE);
        try {
            List<String> row = csvReader.read();
            for (;;) {
                row = csvReader.read();
                if (row == null) {
                    break;
                }
                Date date = new Date(Long.parseLong(row.get(0).trim())*1000);
                double price = Double.parseDouble(row.get(1).trim());
                double volume = Double.parseDouble(row.get(2).trim());
                Transaction transaction = new Transaction(date, price, volume);
                transactions_.add(transaction);
            }
        } finally {
            csvReader.close();
        }

        /* Sort the transactions by date, such that the first transaction is the
         * list is earliest in history. */
        sortTransactionsByDate();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions_;
    }

    private void sortTransactionsByDate() {
        Collections.sort(transactions_, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return (o2.getDate().after(o1.getDate()) ? -1 : (o1.getDate().after(o2.getDate()) ? 1 : 0));
            }
        });
    }


    @Override
    public String toString() {
        return "{transactions=" + getTransactions() + "}";
    }


    private final ArrayList<Transaction> transactions_ = new ArrayList<Transaction>();
}
