import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Historical game database, read in from a CSV file.
 */
public class Database {

    @SuppressWarnings("unused")
    public int addDatabaseFile(File databaseFile) throws IOException {

        Map<Date, Ticker> tickers = new HashMap<Date, Ticker>();
        for (Ticker ticker: tickers_) {
            tickers.put(ticker.getDate(), ticker);
        }

        int lineCount = 0;
        CsvListReader csvReader = new CsvListReader(new FileReader(databaseFile), CsvPreference.STANDARD_PREFERENCE);
        try {
            for (;; lineCount++) {
                List<String> row = csvReader.read();
                if (row == null) {
                    break;
                }
                long unixTime = Long.parseLong(row.get(0).trim());
                Date date = new Date(Long.parseLong(row.get(0).trim()) * 1000);
                double open = Double.parseDouble(row.get(1).trim());
                double high = Double.parseDouble(row.get(2).trim());
                double low = Double.parseDouble(row.get(3).trim());
                double close = Double.parseDouble(row.get(4).trim());
                double volumeBtc = Double.parseDouble(row.get(5).trim());
                double volumeEur = Double.parseDouble(row.get(6).trim());
                double weightedPrice = Double.parseDouble(row.get(7).trim());
                Ticker ticker = new Ticker(date, weightedPrice);
                tickers.put(date, ticker);
            }
        } finally {
            csvReader.close();
        }
        tickers_ = new ArrayList<Ticker>(tickers.values());

        /* Sort the tickers by date, such that the first ticker is the
         * list is earliest in history. */
        sortTickersByDate();

        return lineCount;
    }

    public ArrayList<Ticker> getTickers() {
        return tickers_;
    }

    private void sortTickersByDate() {
        Collections.sort(tickers_, new Comparator<Ticker>() {
            @Override
            public int compare(Ticker o1, Ticker o2) {
                return (o2.getDate().after(o1.getDate()) ? -1 : (o1.getDate().after(o2.getDate()) ? 1 : 0));
            }
        });
    }

    @Override
    public String toString() {
        return String.format("{size=%d, first=%s, last=%s}", tickers_.size(),
            Ticker.DATE_FORMAT.format(tickers_.get(0).getDate()),
            Ticker.DATE_FORMAT.format(tickers_.get(tickers_.size() - 1).getDate()));
    }

    private ArrayList<Ticker> tickers_ = new ArrayList<Ticker>();
}
