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

    @SuppressWarnings("unused")
    public int addDatabaseFile(File databaseFile) throws IOException {

        int lineCount = 0;
        CsvListReader csvReader = new CsvListReader(new FileReader(databaseFile), CsvPreference.STANDARD_PREFERENCE);
        try {
            List<String> row = csvReader.read();
            for (;;lineCount++) {
                row = csvReader.read();
                if (row == null) {
                    break;
                }
                Date date = new Date(Long.parseLong(row.get(0).trim()) * 1000);
                double open = Double.parseDouble(row.get(1).trim());
                double high = Double.parseDouble(row.get(2).trim());
                double low = Double.parseDouble(row.get(3).trim());
                double close = Double.parseDouble(row.get(4).trim());
                double volumeBtc = Double.parseDouble(row.get(5).trim());
                double volumeEur = Double.parseDouble(row.get(6).trim());
                double weightedPrice = Double.parseDouble(row.get(7).trim());
                Transaction transaction = new Transaction(date, weightedPrice);
                transactions_.add(transaction);
            }
        } finally {
            csvReader.close();
        }

        /* Sort the transactions by date, such that the first transaction is the
         * list is earliest in history. */
        sortTransactionsByDate();

        return lineCount;
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
        return String.format("{size=%d, first=%s, last=%s}", transactions_.size(),
            Transaction.DATE_FORMAT.format(transactions_.get(0).getDate()),
            Transaction.DATE_FORMAT.format(transactions_.get(transactions_.size() - 1).getDate()));
    }

    private final ArrayList<Transaction> transactions_ = new ArrayList<Transaction>();
}
