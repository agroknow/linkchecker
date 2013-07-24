/*
 * @author Mathioudakis Theodore
 */



import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class stores data for every domain. Every Domain instance contains a domain-name and all
 * the URL-beans from this specific domain.
 */
public class Domain
{
    private static Domain instance ;
    private String domainName;
    private ConcurrentLinkedQueue<URLBean> domainQueue;


    public Domain(String domainName)
    {
        this.domainName = domainName;
        domainQueue = new ConcurrentLinkedQueue<URLBean>();
    }

    public Domain()
    {
        //this.domainName = domainName;
        domainQueue = new ConcurrentLinkedQueue<URLBean>();
    }


    public static Domain getInstance()
		{
	        
	            synchronized(DomainQueueManager.class)
	            {
	                if (instance == null)
	                {
	                    instance = new Domain() ;
	                }
	            }
	      
	        return instance ;
	    }

    
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public synchronized ConcurrentLinkedQueue<URLBean> getDomainQueue()
    {
	return domainQueue ;
    }

}
