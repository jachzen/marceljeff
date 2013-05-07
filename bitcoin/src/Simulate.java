import java.io.File;

public class Simulate {

    public static void main(String[] args) {
        Database database = new Database();
        try {
            for (String file : IConfiguration.DATABASES) {
                database.addDatabaseFile(new File(file));
            }
            IExchange exchange = (IExchange) IConfiguration.EXCHANGE_CLASS.getConstructor().newInstance();
            IAlgorithm algorithm = (IAlgorithm) IConfiguration.ALGORITHM_CLASS.getConstructor().newInstance();
            new Engine().run(database, exchange, algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// TODO Jeff: You are so gay