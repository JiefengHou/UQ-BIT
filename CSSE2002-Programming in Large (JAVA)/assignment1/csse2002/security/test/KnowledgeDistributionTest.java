package csse2002.security.test; 
  
import org.junit.Assert; 
import org.junit.Test; 
  
import java.util.*; 
import csse2002.math.*; 
import csse2002.security.*; 
  
/** 
 * Basic tests for the {@link KnowledgeDistribution} implementation class. A 
 * much much more extensive test suite will be performed for assessment of your 
 * code, but this should get you started. 
 */
public class KnowledgeDistributionTest { 
  
    /** 
     * A basic test of the first two constructors. 
     */
    @Test
    public void basicConstructorTests() { 
  
        // check constructor with no parameters 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        // check that the weight of the distribution is zero. 
        Assert.assertEquals(BigFraction.ZERO, k.weight()); 
        // check that the weight of some knowledge-state is zero. 
        Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2))); 
        // check toString method 
        Assert.assertEquals("{}", k.toString()); 
        // test iteration over elements in the support of the distribution 
        Iterator<BigFraction> it = k.iterator(); 
        Assert.assertFalse(it.hasNext()); 
        Assert.assertTrue(k.checkInv()); 
  
        // check point-distribution constructor 
  
        BigFraction p = new BigFraction(1, 3); 
        k = new KnowledgeDistribution(p); 
        // check that the weight of the distribution is one. 
        Assert.assertEquals(BigFraction.ONE, k.weight()); 
        // check that the weight of knowledge-state p is one. 
        Assert.assertEquals(BigFraction.ONE, k.weight(p)); 
        // check that the weight of some other knowledge-state is zero. 
        Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2))); 
        // check toString method 
        Assert.assertEquals("{{true@1/3, false@2/3}@1}", k.toString()); 
        // test iteration over elements in the support of the distribution 
        it = k.iterator(); 
        Assert.assertTrue(it.hasNext()); 
        Assert.assertEquals(p, it.next()); 
        Assert.assertFalse(it.hasNext()); 
        Assert.assertTrue(k.checkInv()); 
  
        // test copy distribution constructor 
          
        KnowledgeDistribution kd = new KnowledgeDistribution(k); 
        Assert.assertEquals(kd.weight(), k.weight()); 
        Assert.assertEquals(BigFraction.ONE, kd.weight()); 
        Assert.assertEquals(BigFraction.ONE, kd.weight(p)); 
        Assert.assertEquals(BigFraction.ZERO, kd.weight(new BigFraction(1, 5))); 
        Assert.assertEquals("{{true@1/3, false@2/3}@1}", kd.toString()); 
        Iterator<BigFraction> itd = kd.iterator(); 
        itd = kd.iterator(); 
        Assert.assertTrue(itd.hasNext()); 
        Assert.assertEquals(p, itd.next()); 
        Assert.assertFalse(itd.hasNext()); 
        Assert.assertTrue(kd.checkInv()); 
    } 
  
    /** 
     * Test that one of the exceptions for the point-distribution constructor is 
     * correctly thrown. 
     */
    @Test(expected = InvalidProbabilityException.class) 
    public void constructorInvalidProbabilityTest() { 
        BigFraction p = new BigFraction(4, 3); 
        KnowledgeDistribution k = new KnowledgeDistribution(p); 
    } 
  
    /** 
     * A basic test of the addition method. 
     */
    @Test
    public void basicAdditionTest() { 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(1, 2); 
  
        BigFraction p2 = new BigFraction(1, 8); 
        BigFraction w2 = new BigFraction(1, 3); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.add(p2, w2); 
        // check the weight of the distribution 
        Assert.assertEquals(new BigFraction(5, 6), k.weight()); 
        // check the weight of knowledge-state p1. 
        Assert.assertEquals(w1, k.weight(p1)); 
        // check the weight of knowledge-state p2. 
        Assert.assertEquals(w2, k.weight(p2)); 
        // check that the weight of some other knowledge-state is zero. 
        Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(1, 2))); 
        // check toString method 
        Assert.assertEquals( 
                "{{true@1/8, false@7/8}@1/3, {true@1/4, false@3/4}@1/2}", 
                k.toString()); 
        // test iteration over elements in the support of the distribution 
        Iterator<BigFraction> it = k.iterator(); 
        Assert.assertTrue(it.hasNext()); 
        Assert.assertEquals(p2, it.next()); 
        Assert.assertTrue(it.hasNext()); 
        Assert.assertEquals(p1, it.next()); 
        Assert.assertFalse(it.hasNext()); 
        Assert.assertTrue(k.checkInv()); 
    } 
  
    /** 
     * Test that one of the exceptions for the addition method is correctly 
     * thrown. 
     */
    @Test(expected = InvalidKnowledgeDistributionException.class) 
    public void addTooMuchWeightTest() { 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(3, 4); 
  
        BigFraction p2 = new BigFraction(1, 8); 
        BigFraction w2 = new BigFraction(1, 3); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.add(p2, w2); 
    } 
  
    /** 
     * A basic test of the update method. 
     */
    @Test
    public void basicUpdateTest() { 
        BigFraction p = new BigFraction(1, 2); 
        KnowledgeDistribution k = new KnowledgeDistribution(p); 
  
        ConditionalTwoCoinChannel c1 = new ConditionalTwoCoinChannel( 
                p, 
                new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4))); 
        ConditionalTwoCoinChannel c2 = new ConditionalTwoCoinChannel( 
                new BigFraction(3, 4), new TwoCoinChannel( 
                        new BigFraction(1, 3), new BigFraction(1, 2))); 
  
        k.update(c1); 
        // check toString method 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@3/4, false@1/4}@1/2}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
  
        k.update(c2); 
        // check toString method 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5, false@1/5}@5/16}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
    } 
      
    @Test
    public void AdvancedAdditionSubtractionTests() { 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(1, 2); 
  
        BigFraction p2 = new BigFraction(1, 4); 
        BigFraction w2 = new BigFraction(1, 6); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.add(p2, w2); 
          
        BigFraction p3 = new BigFraction(2, 4); 
        BigFraction w3 = new BigFraction(1, 6); 
          
        k.add(p3, w3); 
        // check the weight of the distribution 
        Assert.assertEquals(new BigFraction(5, 6), k.weight()); 
        // check the weight of knowledge-state p1. 
        Assert.assertEquals(w1.add(w2), k.weight(p1)); 
        // check the weight of knowledge-state p2. 
        Assert.assertEquals(w2.add(w1), k.weight(p2)); 
        Assert.assertEquals(w3, k.weight(p3)); 
        // check that the weight of some other knowledge-state is zero. 
        Assert.assertEquals(BigFraction.ZERO, k.weight(new BigFraction(3, 4))); 
        // check toString method 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@2/3, {true@1/2, false@1/2}@1/6}", 
                k.toString()); 
          
        BigFraction p4 = new BigFraction(3, 5); 
        BigFraction w4 = new BigFraction(1, 6); 
          
        k.add(p4, w4); 
          
        Assert.assertEquals(new BigFraction(1, 1), k.weight()); 
        Assert.assertEquals(BigFraction.ONE, k.weight()); 
          
        k.subtract(p2,w2); 
        // check the weight of the distribution 
        Assert.assertEquals(new BigFraction(5, 6), k.weight()); 
        // check the weight of knowledge-state p1. 
        Assert.assertEquals(w1, k.weight(p1)); 
        // check toString method 
        Assert.assertEquals( 
            "{{true@1/4, false@3/4}@1/2, {true@1/2, false@1/2}@1/6, " + 
            "{true@3/5, false@2/5}@1/6}", k.toString()); 
          
        k.subtract(p4, w4); 
        // check the weight of the distribution 
        Assert.assertEquals(new BigFraction(4, 6), k.weight()); 
        // check toString method 
        Assert.assertEquals( 
            "{{true@1/4, false@3/4}@1/2, {true@1/2, false@1/2}@1/6}"
            , k.toString()); 
  
          
        //Zero weight and Zero probability tests 
        p4 = new BigFraction(1, 4); 
        w4 = BigFraction.ZERO; 
          
        k.add(p4, w4); 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@1/2, false@1/2}@1/6}"
                , k.toString()); 
        k.subtract(p4, w4); 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@1/2, false@1/2}@1/6}"
                , k.toString()); 
          
        p4 = BigFraction.ZERO; 
        w4 = new BigFraction(1, 4); 
          
        k.add(p4, w4); 
        Assert.assertEquals( 
                "{{true@0, false@1}@1/4, {true@1/4, false@3/4}@1/2, {true@1/2, " + 
                "false@1/2}@1/6}", k.toString()); 
        k.subtract(p4, w4); 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@1/2, false@1/2}@1/6}"
                , k.toString()); 
    } 
  
    @Test(expected = InvalidKnowledgeDistributionException.class) 
    public void removeTooMuchWeightTest() { 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(1, 3); 
  
        BigFraction p2 = new BigFraction(1, 4); 
        BigFraction w2 = new BigFraction(3, 4); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.subtract(p2, w2); 
    } 
  
    @Test(expected = InvalidKnowledgeDistributionException.class) 
    public void addMoreThanOneTest() { 
        //Same knowledge state case 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(1, 2); 
  
        BigFraction p2 = new BigFraction(1, 4); 
        BigFraction w2 = new BigFraction(4, 6); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.add(p2, w2); 
    } 
      
    @Test(expected = InvalidKnowledgeDistributionException.class) 
    public void removeMoreThanOneTest() { 
        //Same knowledge state case 
        BigFraction p1 = new BigFraction(1, 4); 
        BigFraction w1 = new BigFraction(1, 2); 
  
        BigFraction p2 = new BigFraction(1, 4); 
        BigFraction w2 = new BigFraction(4, 6); 
  
        KnowledgeDistribution k = new KnowledgeDistribution(); 
        k.add(p1, w1); 
        k.subtract(p2, w2); 
    } 
      
      
    //requires your add and subtract methods to work 
    @Test
    public void zeroWeightTests(){ 
        KnowledgeDistribution kd = new KnowledgeDistribution(); 
          
        BigFraction p1 = new BigFraction(1,2); 
        BigFraction w1 = BigFraction.ZERO; 
          
        kd.add(p1, w1); 
          
        Assert.assertEquals(BigFraction.ZERO, kd.weight()); 
        Assert.assertEquals(BigFraction.ZERO, kd.weight(p1)); 
          
        BigFraction p2 = BigFraction.ZERO; 
        BigFraction w2 = new BigFraction(1,2); 
          
        kd.add(p2, w2); 
          
        Assert.assertEquals(new BigFraction(1,2), kd.weight()); 
        Assert.assertEquals(new BigFraction(1,2), kd.weight(p2)); 
          
        p2 = BigFraction.ZERO; 
        w2 = new BigFraction(1,5); 
          
        kd.add(p2,w2); 
          
        Assert.assertEquals(new BigFraction(7,10), kd.weight()); 
        Assert.assertEquals(new BigFraction(7,10), kd.weight(p2)); 
          
        kd.subtract(p1, w1); 
        Assert.assertEquals("{{true@0, false@1}@7/10}",kd.toString()); 
        kd.subtract(w1, p1); 
        Assert.assertEquals("{{true@0, false@1}@1/5}",kd.toString()); 
          
        kd.add(w1, w1); 
        Assert.assertEquals("{{true@0, false@1}@1/5}",kd.toString()); 
          
        kd.add(w1.complement(), w1); 
        Assert.assertEquals("{{true@0, false@1}@1/5}",kd.toString()); 
    } 
      
    @Test
    public void advancedUpdateTest() { 
        //setup same two knowledge states as previously test 
        BigFraction p = new BigFraction(1, 2); 
        KnowledgeDistribution k = new KnowledgeDistribution(p); 
  
        ConditionalTwoCoinChannel c1 = new ConditionalTwoCoinChannel( 
                p, 
                new TwoCoinChannel(new BigFraction(3, 4), new BigFraction(1, 4))); 
        ConditionalTwoCoinChannel c2 = new ConditionalTwoCoinChannel( 
                new BigFraction(3, 4), new TwoCoinChannel( 
                        new BigFraction(1, 3), new BigFraction(1, 2))); 
          
        k.update(c1); 
        k.update(c2); 
          
          
        //update with empty channel 
        ConditionalTwoCoinChannel cempty = new ConditionalTwoCoinChannel( 
                new BigFraction(1, 4), new TwoCoinChannel( 
                        BigFraction.ZERO, BigFraction.ZERO)); 
        k.update(cempty); 
          
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5, false@1/5}@5/16}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
          
        //update with channel of 100% 
        ConditionalTwoCoinChannel cfull = new ConditionalTwoCoinChannel( 
                new BigFraction(1, 4), new TwoCoinChannel( 
                        BigFraction.ONE, BigFraction.ONE)); 
          
        k.update(cfull); 
          
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@2/3, false@1/3}@3/16, {true@4/5, false@1/5}@5/16}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
          
        //update with two more states 
        ConditionalTwoCoinChannel c3 = new ConditionalTwoCoinChannel( 
                new BigFraction(2,3), new TwoCoinChannel( 
                        new BigFraction(1, 5), new BigFraction(1, 6))); 
          
        ConditionalTwoCoinChannel c4 = new ConditionalTwoCoinChannel( 
                new BigFraction(4,5), new TwoCoinChannel( 
                        new BigFraction(1, 3), new BigFraction(1, 2))); 
          
        k.update(c3); 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@48/73, false@25/73}@73/480, {true@12/17, false@5/17}@17/480, {true@4/5, false@1/5}@5/16}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
          
        k.update(c4); 
        Assert.assertEquals( 
                "{{true@1/4, false@3/4}@1/2, {true@48/73, false@25/73}@73/480, {true@12/17, false@5/17}@17/480, {true@8/11, false@3/11}@11/96, {true@16/19, false@3/19}@19/96}", 
                k.toString()); 
        Assert.assertTrue(k.checkInv()); 
          
          
    } 
}