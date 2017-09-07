package csse2002.security.test; 
  
import org.junit.Assert; 
import org.junit.Test; 
//Added. Not included in original assignment template. 
import static org.hamcrest.CoreMatchers.*; 
  
import csse2002.math.*; 
import csse2002.security.*; 
  
/** 
 * Write your JUnit4 tests here for the {@link TwoCoinChannel} implementation 
 * class. An extensive test suite of our own will be executed for assessment of 
 * your code. 
 *  
 * If you are a CSSE7023 student then you must write and a systematic and 
 * understandable JUnit4 test suite for the {@link TwoCoinChannel} class in this 
 * file. You must also include this source file in your submission. 
 *  
 */
public class TwoCoinChannelTest { 
    //TODO create a hashcode test 
      
    /** 
     * Test constructor throws exception on either probability invalid 
     */
    @Test(expected = InvalidProbabilityException.class) 
    public void testProbabilityException() { 
        BigFraction c1; 
        BigFraction c2; 
        TwoCoinChannel channel; 
        //Test c1 = 2 fails 
        c1 = new BigFraction(2); 
        c2 = new BigFraction(1, 2); 
        channel = new TwoCoinChannel(c1, c2); 
          
        //Test c2 = -1 fails 
        c1 = new BigFraction(1); 
        c2 = new BigFraction(-1); 
        channel = new TwoCoinChannel(c1, c2); 
    } 
  
      
    /** 
     * Test constructor throws exception on either argument empty 
     */
    @Test(expected = NullPointerException.class) 
    public void testNullException() { 
        TwoCoinChannel channel = new TwoCoinChannel(null, null); 
    } 
      
    /** 
     * Test aPriori method throws exception when argument is an invalid probability 
     */
    @Test(expected = InvalidProbabilityException.class) 
    public void testaPrioriProbabilityException() { 
        BigFraction c1 = new BigFraction(1, 2); 
        BigFraction c2 = new BigFraction(1, 2); 
        TwoCoinChannel channel = new TwoCoinChannel(c1, c2); 
        channel.outcomeProbability(new BigFraction(2, 1), true); 
    } 
      
      
    /** 
     * Tests the correctness of the outcome probability 
     */
    @Test
    public void testOutcomeProbability() { 
          
        BigFraction aPriori, outcomeHeads, outcomeTails; 
        TwoCoinChannel channel; 
          
        //test case 1 
        aPriori = new BigFraction(1, 2); 
        channel = new TwoCoinChannel(new BigFraction(3, 4), 
                                     new BigFraction(1, 4)); 
        outcomeHeads = channel.outcomeProbability(aPriori, true); 
        outcomeTails = channel.outcomeProbability(aPriori, false); 
        Assert.assertEquals(new BigFraction(1, 2), outcomeHeads); 
        Assert.assertEquals(new BigFraction(1, 2), outcomeTails); 
        Assert.assertTrue(channel.checkInv()); 
          
        //test case 2 
        aPriori = new BigFraction(3, 4); 
        channel = new TwoCoinChannel(new BigFraction(1, 3), 
                                     new BigFraction(1, 2)); 
        outcomeHeads = channel.outcomeProbability(aPriori, true); 
        outcomeTails = channel.outcomeProbability(aPriori, false); 
        Assert.assertEquals(new BigFraction(3, 8), outcomeHeads); 
        Assert.assertEquals(new BigFraction(5, 8), outcomeTails); 
        Assert.assertTrue(channel.checkInv()); 
    } 
      
    /** 
     * Test that aPosteriori has probability BigInteger.ZERO when 
     * when P(outcome) = 0. 
     */
    @Test
    public void testOutcomeZero() { 
        BigFraction aPriori; 
        TwoCoinChannel channel; 
          
        //Test that if P(outcome) = 0 then aPosteriori is zero 
        aPriori = new BigFraction(1); 
        channel = new TwoCoinChannel(new BigFraction(0), 
                                     new BigFraction(1)); 
        Assert.assertEquals(BigFraction.ZERO, channel.aPosteriori(aPriori, true)); 
    } 
      
  
    /** 
     * Tests the correctness of the aPosteriori method 
     */
    @Test
    public void testSecretProbability() { 
        BigFraction aPriori, c1, c2; 
        TwoCoinChannel channel; 
          
        //test case 1 
        c1 = new BigFraction(3, 4); 
        c2 = new BigFraction(1, 4); 
        channel = new TwoCoinChannel(c1, c2); 
        aPriori = new BigFraction(1, 2); 
          
        Assert.assertEquals(new BigFraction(3, 4), channel.aPosteriori(aPriori, true)); 
        Assert.assertEquals(new BigFraction(1, 4), channel.aPosteriori(aPriori, false)); 
          
        //test case 2 
        c1 = new BigFraction(1, 3); 
        c2 = new BigFraction(1, 2); 
        channel = new TwoCoinChannel(c1, c2); 
        aPriori = new BigFraction(3, 4); 
          
        Assert.assertEquals(new BigFraction(2, 3), channel.aPosteriori(aPriori, true)); 
        Assert.assertEquals(new BigFraction(4, 5), channel.aPosteriori(aPriori, false)); 
  
          
        //Test that if aPriori = 0 then aPosteriori = 0 
        aPriori = new BigFraction(0, 1); 
        channel = new TwoCoinChannel(new BigFraction(1, 2), 
                                     new BigFraction(1, 2)); 
        Assert.assertEquals(BigFraction.ZERO, channel.aPosteriori(aPriori, true)); 
          
        //Test that if P(secret and outcome) = 0 then aPosteriori = 0 
        aPriori = new BigFraction(1, 2); 
        channel = new TwoCoinChannel(new BigFraction(0), 
                                     new BigFraction(1, 2)); 
        Assert.assertEquals(BigFraction.ZERO, channel.aPosteriori(aPriori, true)); 
          
    } 
      
    /** 
     * Tests that toString is properly implemented   
     */
    @Test
    public void testConvertBiasestoStrings() { 
        BigFraction c1 = new BigFraction(3, 4); 
        BigFraction c2 = new BigFraction(1, 4); 
          
        TwoCoinChannel channel = new TwoCoinChannel(c1, c2); 
        Assert.assertEquals("(3/4, 1/4)", channel.toString()); 
          
        //TODO add more cases 
        //TODO test .ZERO case 
    } 
      
    /** 
     * Tests that two channels with the same c1 and c2 are considered equal 
     */
    @Test
    public void testEquality() { 
        BigFraction c1; 
        BigFraction c2; 
        TwoCoinChannel channel1; 
        TwoCoinChannel channel2; 
          
        c1 = new BigFraction(3, 4); 
        c2 = new BigFraction(1, 4); 
          
        channel1 = new TwoCoinChannel(c1, c2); 
        channel2 = new TwoCoinChannel(c1, c2); 
                  
        Assert.assertEquals(channel1, channel2); 
        Assert.assertEquals(channel1.hashCode(), channel2.hashCode()); 
    } 
      
    /** 
     * Tests that two channels with different c1 and c2 are considered unequal 
     */
    @Test
    public void testUnequality() { 
        BigFraction c1 = new BigFraction(3, 4); 
        BigFraction c2 = new BigFraction(1, 4); 
        BigFraction c3 = new BigFraction(0, 1); 
        BigFraction c4 = new BigFraction(2, 4); 
  
        TwoCoinChannel channel1 = new TwoCoinChannel(c1, c2); 
        TwoCoinChannel channel2 = new TwoCoinChannel(c3, c2); 
        TwoCoinChannel channel3 = new TwoCoinChannel(c1, c3); 
        TwoCoinChannel channel4 = new TwoCoinChannel(c3, c4); 
          
        Assert.assertThat(channel1, not(channel2)); 
        Assert.assertThat(channel1, not(channel3)); 
        Assert.assertThat(channel2, not(channel4)); 
        Assert.assertThat(channel3, not(channel4)); 
          
        //Test that null pointer fails to be equal 
        Assert.assertThat(null, not(channel4)); 
    } 
} 
