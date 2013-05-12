/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package to.sparks.mtgox.model;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @Override
    @Before
    public void setUp() {
    }

    @Override
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
