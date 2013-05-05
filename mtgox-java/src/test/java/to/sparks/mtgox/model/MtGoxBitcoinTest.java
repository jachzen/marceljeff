/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package to.sparks.mtgox.model;

import java.math.BigDecimal;
import junit.framework.TestCase;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author SparksG
 */
public class MtGoxBitcoinTest extends TestCase {

    public MtGoxBitcoinTest(String testName) {
        super(testName);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMtGoxBitcoin() {

        MtGoxBitcoin a = new MtGoxBitcoin(100000000L);
        MtGoxBitcoin b = new MtGoxBitcoin(1.0D);
        MtGoxBitcoin c = new MtGoxBitcoin(BigDecimal.valueOf(1.0D));


        assertTrue(a.equals(b));
        assertTrue(a.equals(c));
        assertTrue(b.equals(c));
    }
}
