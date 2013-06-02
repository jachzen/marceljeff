import java.util.ArrayList;

/**
 * We can have diffrent engine types
 * 1. Marketorder Engine (directly sell/buy at marketprice)
 * 2. Normal orders
 * @author mpater
 *
 */
public interface IEngine {
	void run(ArrayList<Double> debugData);
}
