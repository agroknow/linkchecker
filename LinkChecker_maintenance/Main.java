/*
 * @author Mathioudakis Theodore
 */


import java.io.* ;
import java.net.MalformedURLException;
import java.net.URL;
import net.zettadata.simpleparser.*;

import java.util.Properties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

	// ARGS[] contains repository@[0] , broken_dir@[1], ok_dir@[2]
    public static void main(String[] args) throws Exception
    {

    	//         //--read .properties file
    	//         Properties prop = new Properties();
    	// 
    	// 
    	// try {//load a properties file
    	//             InputStream propertiesStream = new FileInputStream("config.properties");
    	// 			            prop.load(propertiesStream);
    	// 			            propertiesStream.close();
    	//             } catch (IOException ex) {ex.printStackTrace();}

        //-----------


        String ROOT_FOLDER = args[0]; //prop.getProperty("repository_folder");

                //--lomParser
                String LOM_FOLDER = ROOT_FOLDER + File.separator;
                //-LOM FOLDER
                File directory = new File(LOM_FOLDER);
        
        
                File subDirectories[] = directory.listFiles();

                System.out.println("Number of directories:"+ directory.listFiles().length);

                int urlsCtr = 0;
                int noHttp = 0;
                int malformedCtr = 0;

                for(int j=0; j<subDirectories.length;j++)
                    {
	
                
					System.out.println("Set: " + subDirectories[j].getName());
                    File subDirectory = new File(LOM_FOLDER + subDirectories[j].getName());
                            File files[] = subDirectory.listFiles();

                                 //changed from AKIF to a repository with jsons (without folders)
                                //no subfolders- File files[] = directory.listFiles();
        
                                SimpleMetadata tmpMetadata = null ;


                                for(int i=0; i<files.length ;i++)
                                {
                                     File tmpFile=files[i];
                                    try
                                    {

										tmpMetadata = SimpleMetadataFactory.getSimpleMetadata( SimpleMetadataFactory.AKIF ) ;
										tmpMetadata.load(LOM_FOLDER + subDirectories[j].getName()+"/"+tmpFile.getName() ) ;
                                    }
                                    catch (ParserException e){System.out.println(e);}

                                    //for every record
                                    for(String identString : tmpMetadata.getIdentifiers())
                                    {
                                       // System.out.println("item: " + identString);
                                        
                                        IdentifierBean ib = new IdentifierBean(identString, subDirectories[j].getName()); //IdentifierBean( String identifier, String set )
                                        
                                        
                                        //for every url
                                        for(String locString : tmpMetadata.getLocations())//urls
                                        {
                                            urlsCtr++;

                                            try
                                            {
                                            //storing the locString in the URL in order to parse the domain and use it properly.
                                            URL url = new URL(locString);

                                            if(url.getProtocol().equals("http"))
                                            {
                                            //creating a url-bean for putting it in the appropriate Queue
                                            URLBean tmpURLBean = new URLBean(url, tmpFile.getName(), subDirectories[j].getName() , identString);
                                                
                                            
                                                //add url in current IdentifierBean
                                                ib.addURLBean(tmpURLBean);
                                                
                                                //DomainQueueManager.getInstance().addUrl(tmpURLBean);
                         
                                            }
                                            else 
                                            {
                                                //ADD a flag to check the rejected caused from the protocol
//                                                URLBean tmpURLBean = new URLBean(url, tmpFile.getName(), subDirectories[j].getName() , identString);
//                                                
//                                                DomainQueueManager.getInstance().addUrl(tmpURLBean);
//                                                noHttp++;
                                            }

                                            }
                                            catch (MalformedURLException e)
                                            {
                                                //in principle we don't have this type of urls in maintenance version
                                                //boolean malwaredCopied = copyRecordsWithMalwareUrls(tmpFile.getName(),args);
                                                //System.out.println(">>>>-------malwaredCopied:" + malwaredCopied);
                                                
                                                malformedCtr ++;
                                                //e.printStackTrace();
                                            }

                                        }
                                        
                                        //after entering all urls of a record in an IdentifierBean we add it in the Queue
                                        DomainQueueManager.getInstance().getIdentBeansQueue().add(ib);
                                        
                                        
                                    }


                                }

                     }

// CHECKING size of nested queues
                    System.out.println("\n\n--------------CHECKING QUEUEs SIZES BEFORE CHECKING URL-------------\n");
                    //System.out.println("DomainQueueManager_____size____ :" + DomainQueueManager.getInstance().getPreChecking().size());
                    System.out.println("getIdentBeansQueue_____size____ :" + DomainQueueManager.getInstance().getIdentBeansQueue().size());
                    int allurls=0;
//                    for( Domain domain : DomainQueueManager.getInstance().getPreChecking().values())
//                    {
//                        System.out.println("domain: |"+domain.getDomainName()+"|:" + domain.getDomainQueue().size());
//                        allurls += domain.getDomainQueue().size();
//                    }
//                    System.out.println("\n\nTotal urls:" + urlsCtr + "\nMalformed urls:"+ malformedCtr + "\nnoHTTP urls:"+ noHttp);
//                    System.out.println("\n----URLS for testing : " + allurls);


//PRINT START TIME
                   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                   //get current date time with Calendar()
                   Calendar cal_start = Calendar.getInstance();
                   System.out.println(dateFormat.format(cal_start.getTime()));


// THREADS
                  System.out.println("\n\n\n--------------THREADS-------------");

				// ARGS[] contains repository@[0] , broken_dir@[1], ok_dir@[2], edited_dir[3]
                  PoolManager.getInstance().checkUrls(args); 


                  while(!PoolManager.getInstance().getExecutor().isTerminated())//Returns true if all tasks have completed following shut down.
                  {/*System.out.println("0");*/}



                    System.out.println("\n\n--------------------------------END-------------------------------------------");
//RESULTS
                            // display test results

//                      System.out.println("\nDomainQueueManager_____size____ :" + DomainQueueManager.getInstance().getCheckingQueue().size());

//                      System.out.println("\nFinalQueueOK_____size____ :" +DomainQueueManager.getInstance().getFinalQueueOk().size());

//                      System.out.println("\nbroken links:");
//                      int brokenctr = 0; //counter for brokenlinks
//                      int minusOneError = 0; //counter for -1 error - check isUrlBroken() method in URLChecker
//                      int otherErrors = 0; //counter for other errors

//                        for( URLBean item : DomainQueueManager.getInstance().getOutput().values() )
//                            {
//                            System.out.println( "broken_" + item.isBroken() + "|ResponseCode_"+item.getResponseCode()+"|"+ item.getUrl()) ;
//                             if(item.isBroken())
//                             {
//                                 brokenctr++;
//                                 if(item.getResponseCode()==-1 ){minusOneError++;}
//                                 if(item.getResponseCode()!=-1 && item.getResponseCode()!=200 ){otherErrors++;}
//
//                             }
//                            }


                     // System.out.println("\n\ngetOutputNoHash().size():" + DomainQueueManager.getInstance().getOutputNoHash().size());
                     // System.out.println("urlsCtr:"+urlsCtr);
                     // System.out.println("PoolManager.getInstance().getExecutor().isTerminated():"+PoolManager.getInstance().getExecutor().isTerminated());
                     //System.out.println("\n___Output.size____ :" +DomainQueueManager.getInstance().getOutput().size()+
                     //      " \n\nBroken links sum:"+brokenctr+"\n-1s:"+minusOneError+"\nOthers:"+otherErrors+"\n\n___END___");
                     //System.out.println("\n Terminated:" + PoolManager.getInstance().getExecutor().isTerminated());

                      // PRINT START - FINISH TIME
//			 Calendar cal_end = Calendar.getInstance();
//               System.out.println("\nstart:"+dateFormat.format(cal_start.getTime())+" | | finish:"+ dateFormat.format(cal_end.getTime()));



    }//end of main
    
    
    
    
    public static boolean copyRecordsWithMalwareUrls(String filename, String[] directories)
    {
        System.out.println("--------Copy Record with Malwared Url");

        InputStream inStream = null;
        OutputStream outStream = null;
        
        
        File src = new File(directories[0]+ File.separator + filename);
        File dest = new File(directories[1]+ File.separator + filename);
        
        try{
    	    inStream = new FileInputStream(src);
    	    outStream = new FileOutputStream(dest);
    	    byte[] buffer = new byte[1024];
    	    int length;
    	    //copy the file content in bytes
    	    while ((length = inStream.read(buffer)) > 0)
            {outStream.write(buffer, 0, length);}
            
    	    inStream.close();
    	    outStream.close();
    	    //System.out.println("File is copied successfully");
    	}
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
            }
        
        return true;
        
        
    }
    
    
    
}//end of Main
