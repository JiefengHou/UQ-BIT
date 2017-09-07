package csse2002.security;

/**
 * An exception indicating an invalid KnowledgeDistribution
 */
@SuppressWarnings("serial")
public class InvalidKnowledgeDistributionException extends RuntimeException {

	   public InvalidKnowledgeDistributionException(){
	        super();
	    }
		
	    public InvalidKnowledgeDistributionException(String s){
	        super(s);
	    }

}
