/*
 * @author Mathioudakis Theodore
 */



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PoolManager
{

    private static PoolManager instance ;
    private ExecutorService executor ;
    private int THREAD_NUMBER = 1000;
	
    public static PoolManager getInstance()
		{
                    synchronized (PoolManager.class)
                    {
                        if (instance == null)
                        {
                            instance = new PoolManager();
                        }
                    }

                    return instance ;
                }


   public void checkUrls(String[] directories)
                {
                    System.out.println("Number of threads : " + THREAD_NUMBER);
                    executor = Executors.newFixedThreadPool( THREAD_NUMBER ) ;

                    //transfer urlbeans from prechecking to checking queue
                    DomainQueueManager.getInstance().hashtableToQueue();

                    checkUrlThread worker;

                    //The number of threads should be the size of the pool (i.e., the executor).
                    int PoolSize = THREAD_NUMBER;
                    for (int i=0; i < PoolSize; i++)
                    {
                            worker = new checkUrlThread(directories) ;
                            executor.submit(worker);
                    }

                    
                    //ends thread
                     executor.shutdown();
                    
                     while (!executor.isTerminated())//Returns true if all tasks have completed following shut down.
                     {/*System.out.println("1 : " + executor.toString());*/}
                     

                }


    public ExecutorService getExecutor()
    {
        return executor;
    }

   
}
