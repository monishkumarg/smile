/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/
package smile.clustering;

import smile.validation.RandIndex;
import smile.validation.AdjustedRandIndex;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haifeng Li
 */
public class DeterministicAnnealingTest {
    
    public DeterministicAnnealingTest() {
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

    /**
     * Test of learn method, of class DeterministicAnnealing.
     */
    @Test
    public void testUSPS() {
        System.out.println("USPS");
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setResponseIndex(new NominalAttribute("class"), 0);
        try {
            AttributeDataset train = parser.parse("USPS Train", this.getClass().getResourceAsStream("/smile/data/usps/zip.train"));
            AttributeDataset test = parser.parse("USPS Test", this.getClass().getResourceAsStream("/smile/data/usps/zip.test"));

            double[][] x = train.toArray(new double[train.size()][]);
            int[] y = train.toArray(new int[train.size()]);
            double[][] testx = test.toArray(new double[test.size()][]);
            int[] testy = test.toArray(new int[test.size()]);
            
            DeterministicAnnealing annealing = new DeterministicAnnealing(x, 10, 0.8);
            
            AdjustedRandIndex ari = new AdjustedRandIndex();
            RandIndex rand = new RandIndex();
            double r = rand.measure(y, annealing.getClusterLabel());
            double r2 = ari.measure(y, annealing.getClusterLabel());
            System.out.format("Training rand index = %.2f%%\tadjusted rand index = %.2f%%\n", 100.0 * r, 100.0 * r2);
            assertTrue(r > 0.75);
            assertTrue(r2 > 0.25);
            
            int[] p = new int[testx.length];
            for (int i = 0; i < testx.length; i++) {
                p[i] = annealing.predict(testx[i]);
            }
            
            r = rand.measure(testy, p);
            r2 = ari.measure(testy, p);
            System.out.format("Testing rand index = %.2f%%\tadjusted rand index = %.2f%%\n", 100.0 * r, 100.0 * r2);
            assertTrue(r > 0.75);
            assertTrue(r2 > 0.3);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
