import java.io.File;

public class Simulate {

    public static void main(String[] args) {
        Database database = new Database();
        try {
            for (String file : IConfiguration.DATABASES) {
                database.addDatabaseFile(new File(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
