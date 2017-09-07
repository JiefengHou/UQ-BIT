package csse2002.security.test; 
  
import org.junit.Assert; 
import org.junit.Test; 
  
import csse2002.math.*; 
import csse2002.security.*; 
  
/** 
 * Write your JUnit4 tests here for the {@link ConditionalTwoCoinChannel} 
 * implementation class. An extensive test suite of our own will be executed for 
 * assessment of your code. 
 */
public class ConditionalTwoCoinChannelTest { 
  
    /** 
     * A basic test of the constructor 
     */
    @Test
    public void testConstrution() { 
        BigFraction aPriori = new BigFraction(1, 2); 
        TwoCoinChannel channel = new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4)); 
        ConditionalTwoCoinChannel conditionalChannel = new ConditionalTwoCoinChannel(aPriori, channel); 
          
         //note that this relies on the proper implementation and testing of TwoCoinChannel.equals 
        Assert.assertEquals(channel, conditionalChannel.getTwoCoinChannel()); 
        Assert.assertEquals(aPriori, conditionalChannel.getCondition()); 
        Assert.assertEquals(channel.outcomeProbability(aPriori, true), 
                            conditionalChannel.outcomeProbability(true)); 
    } 
      
      
    /** 
     * Test that the constructor throws an exception for invalid probabilities 
     */
    @Test(expected = InvalidProbabilityException.class) 
    public void testProbailityException() { 
        BigFraction p = new BigFraction(2, 1); 
        TwoCoinChannel channel = new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4)); 
        ConditionalTwoCoinChannel conditionalChannel = new ConditionalTwoCoinChannel(p, channel); 
    } 
      
    /** 
     * Test that the constructor throws an exception if either argument is empty 
     */
    @Test(expected = NullPointerException.class) 
    public void testNullException() { 
        ConditionalTwoCoinChannel conditionalChannel = new ConditionalTwoCoinChannel(null, null); 
    } 
      
    /** 
     * Tests that two objects are equal if they have the same state and channel 
     */
    @Test
    public void testEquality() { 
        BigFraction c1 = new BigFraction(3, 4); 
        BigFraction c2 = new BigFraction(1, 4); 
        BigFraction aPriori = new BigFraction(1, 2); 
        TwoCoinChannel channel = new TwoCoinChannel(c1, c2); 
          
        ConditionalTwoCoinChannel conditionalChannel1 = new ConditionalTwoCoinChannel(aPriori, channel); 
        ConditionalTwoCoinChannel conditionalChannel2 = new ConditionalTwoCoinChannel(aPriori, channel); 
          
        Assert.assertEquals(conditionalChannel1, conditionalChannel2); 
        Assert.assertTrue(conditionalChannel1.checkInv()); 
        Assert.assertTrue(conditionalChannel2.checkInv()); 
    } 
      
    /** 
     * Tests that toString is properly implemented 
     */
    @Test
    public void testStringFormatting() { 
        BigFraction aPriori = new BigFraction(1, 2); 
        TwoCoinChannel channel = new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4)); 
        ConditionalTwoCoinChannel conditionalChannel = new ConditionalTwoCoinChannel(aPriori, channel); 
          
        //TODO check this against docs 
        Assert.assertEquals("if true@1/2 then (3/4, 1/4)", conditionalChannel.toString()); 
          
        Assert.assertTrue(conditionalChannel.checkInv()); 
    } 
  
} 
