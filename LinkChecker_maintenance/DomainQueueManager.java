/*
 * @author Mathioudakis Theodore
 */



import java.util.concurrent.ConcurrentLinkedQueue ;
import java.util.concurrent.ConcurrentHashMap ;

/*
 *This class centrally manages the URLs
 *Contains the final queue with urls
 */
public class DomainQueueManager {



		private static DomainQueueManager instance ;

                //this Hashtable will contain all the Domains item before checking. domainUrl is the key, a Domain is the Value.
				private ConcurrentHashMap<String, Domain> preChecking ;

                //this queue will contain all the Domains checking
                private ConcurrentLinkedQueue<Domain> checkingQueue;

                //this Hashtable will contain all the URLBeans after checking. url is the key and the UrlBean is the Value.
				private ConcurrentHashMap<String, URLBean> output ;

				//this Hashtable will contain all the URLBeans after checking. url is the key and the UrlBean is the Value.
				private ConcurrentLinkedQueue<URLBean> outputNoHash ;

    
                //exp- this Hashtable will contain all the URLBeans after checking. url is the key and the UrlBean is the Value.
                private ConcurrentLinkedQueue<IdentifierBean> identBeansQueue ;



		protected DomainQueueManager()
		{
			preChecking = new ConcurrentHashMap<String, Domain>() ;
                        checkingQueue = new ConcurrentLinkedQueue<Domain>() ;

                        output = new ConcurrentHashMap<String, URLBean>() ;

						outputNoHash = new ConcurrentLinkedQueue<URLBean>() ;
            
                        //exp- new queue
                        identBeansQueue = new ConcurrentLinkedQueue<IdentifierBean>();

//                        finalQueueOk = new ConcurrentLinkedQueue<URLBean>() ;
//                        finalQueueBroken = new ConcurrentLinkedQueue<URLBean>() ;

		}

		public static DomainQueueManager getInstance()
		{
                    synchronized (DomainQueueManager.class)
                    {
                        if (instance == null) {
                            instance = new DomainQueueManager();
                        }
                    }

                    return instance ;
                }

                public synchronized ConcurrentHashMap<String,Domain> getPreChecking()
                {
                    return preChecking ;
                }

                 public synchronized ConcurrentLinkedQueue<Domain> getCheckingQueue()
                {
                    return checkingQueue ;
                }

                public synchronized ConcurrentHashMap<String,URLBean> getOutput()
                {
                    return output ;
                }


                public synchronized ConcurrentLinkedQueue<URLBean> getOutputNoHash()
                {
                    return outputNoHash ;
               	}
    
                public synchronized ConcurrentLinkedQueue<IdentifierBean> getIdentBeansQueue()
                {
                        return identBeansQueue ;
                }

/*
 *addUrl() function takes care of put the URlBeans in the appropriate Queue (of a Domain object)
 *returns boolean
 */
                public boolean addUrl(URLBean urlBean)
                {


                    // tempdomain holds the domain of the urlBean we attemp to enter
                    String tempdomain = urlBean.getUrl().getHost();
                            if(preChecking.containsKey(tempdomain))
                            {
                                //System.out.println("__1.Contains Key___");
                                preChecking.get(tempdomain).getDomainQueue().add(urlBean);
                                return true;
                            }
                            else
                            {
                                //System.out.println("__2.Doesn't contains Key___");
                                Domain tmpDomain = new Domain(tempdomain); //creates new Domain
                                tmpDomain.getDomainQueue().add(urlBean); //adds the URLBean in the Queue
                                preChecking.put(tempdomain, tmpDomain); //adds the Domain in the PreChecking List
                                return false;
                            }

                }

                //transfer urlbeans from Prechecking to Checking queue
                public boolean hashtableToQueue()
                {
                    for(Domain domain : preChecking.values())
                    checkingQueue.add(domain);

                    return true;

                }

           



}
