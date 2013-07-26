/*
 * @author Mathioudakis Theodore
 */



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// FIXME : Class names are always capitalized.
public class checkUrlThread implements Runnable
{
	private String[] directories;
     public checkUrlThread(String[] directories)
    {
	 this.directories = directories;
	 }

    public synchronized void run()
    {

		 //System.out.println("__:start checkUrlThread");


                   while(!DomainQueueManager.getInstance().getCheckingQueue().isEmpty())
                   {
                       Domain domain = DomainQueueManager.getInstance().getCheckingQueue().poll();
                        try
                        {
                            checkDomainBean(domain);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }




                       //System.out.println(".CheckingQueue.size: " + DomainQueueManager.getInstance().getCheckingQueue().size());
                       //System.out.println("..Output.size: " + DomainQueueManager.getInstance().getOutput().size());

                   }

    }

  // It is a already a different thread per domain. no need to start more thread to check the urls of this domain. 
  private void checkDomainBean( Domain db ) throws Exception
    {
    	ExecutorService executor ;
    	URLBean ubean ;

    	while( !db.getDomainQueue().isEmpty() )
	{

    		// get the URL Bean
    		ubean = db.getDomainQueue().remove() ;
    		// Prepare the URL Checker
                //System.out.println("..."+db.getDomainName()+".size: " +db.getDomainQueue().size());
//    		URLChecker uchecker = new URLChecker(ubean, directories) ;
    		
			// prepare the Executor
//    		executor = Executors.newSingleThreadExecutor() ;
                try
                {
                    URLChecker.isUrlBroken(ubean, directories);
//                Collection<Callable<Boolean>> list = new ArrayList<Callable<Boolean>>() ;
//    		list.add( uchecker ) ;
//    		List<Future<Boolean>>  futuresList = null ;
//    		try
//    		{
//                    //Executes the given tasks, returning a list of Futures holding their status and results when
//                    //all complete or the timeout expires, whichever happens first. Timeout is 60 seconds
//
//    		   futuresList = executor.invokeAll(list, 30, TimeUnit.SECONDS ) ;
//                   //executor.shutdown();

//		}
//    		catch (InterruptedException e)
//                {
//                    System.out.println("_catch me_");
//                    e.printStackTrace();
//                    //executor.shutdown();
//                }

                //System.out.println("here");


                }catch(Exception e)
                {e.printStackTrace();}
//    			finally
//                {executor.shutdown();}


    }

}


}
///----------------------------------exceptions may need later

//initialize error code - int code=0;
// catch(BindException e)
//            {
//                code=1;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(ConnectException e)
//            {
//                code=2;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(HttpRetryException e)
//            {
//                code=3;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(MalformedURLException e)
//            {
//                code=4;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(NoRouteToHostException e)
//            {
//                code=5;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(PortUnreachableException e)
//            {
//                code=6;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(ProtocolException e)
//            {
//                code=7;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(SocketException e)
//            {
//                code=8;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(SocketTimeoutException e)
//            {
//                code=9;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(UnknownHostException e)
//            {
//                code=10;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }
//            catch(UnknownServiceException e)
//            {
//                code=11;//e.printStackTrace();
//                System.out.println("Code>"+code +" for url: "+thisUrl); return false;
//            }