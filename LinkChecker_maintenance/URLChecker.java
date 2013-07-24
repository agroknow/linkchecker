/*
 * @author Mathioudakis Theodore
 */


import java.io.*; //for File, IOException, (File)InputStream, (File)OutputStream
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//import net.zettadata.linkchecker.url.IdentifierBean;
//import net.zettadata.linkchecker.url.URLBean;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Properties;

public class URLChecker implements Callable<Boolean>
{

    private URLBean urlBean ;

	//NOTE:this array stores the url for the repository@[0]
	private String[] directories;    
    //THE REST USED ONLY IN CHECKING VERSION. NOT IN MAINTENANCE
	//the broken_items_directory@[1]
	//the ok_items_directory@[2]
    //and the edited_items_directory@[3]

	public URLChecker(URLBean urlbean, String[] directories)
    {
        this.urlBean = urlbean;
		this.directories = directories;
    }

	private boolean isUrlBroken()
	{
       
		String tempUrl = urlBean.getUrl().toString();
		DomainQueueManager.getInstance().getOutput().put(tempUrl, urlBean ) ;
        
		boolean result = true ;
		int code = 0 ;


           
            try
            {
			code = ((HttpURLConnection) urlBean.getUrl().openConnection()).getResponseCode();

				 //set response code to check the outcome.
                 //DomainQueueManager.getInstance().getOutput().get( tempUrl ).setResponseCode(code) ;
				 //urlBean.setResponseCode(code) ;


			         if ( code == 200 ) //URL is OK
			           {
			                  result = false ;
			                  //DomainQueueManager.getInstance().getOutput().get( tempUrl ).setBroken( false ) ;
								//DomainQueueManager.getInstance().getOutputNoHash().add(urlBean);
                           System.out.println("$$$$_not updated : " + urlBean.getFilename());

                                    }
                                    else
                                      {
                                          
                                          IdentifierBean ib = new IdentifierBean(urlBean) ;
                                          //DomainQueueManager.getInstance().getIdentBeansQueue().add(ib);
                                          try
                                          {
                                          String akifString = FileUtils.readFileToString( new File( directories[0] + urlBean.getSet()+"/"+ urlBean.getFilename()) ) ;
                                        
                                              String editedContent =  updateAKIF( akifString, ib );
                                              if(editedContent!=null){ System.out.println( "****_updated item : " + urlBean.getFilename());}
                                         
                                              FileUtils.writeStringToFile( new File(directories[0] +"/"+ urlBean.getSet()+"/"+ urlBean.getFilename()) , editedContent);
                                          }catch(Exception e){e.printStackTrace();}
                   
                                      }




		}
            catch (IOException ioe)
            {
                    System.out.println("ioe:"+ioe.getMessage() );
                    //ioe.printStackTrace();
                
                //exp-
                IdentifierBean ib = new IdentifierBean( urlBean ) ;
                //DomainQueueManager.getInstance().getIdentBeansQueue().add(ib);
                
                try
                {
                String akifString = FileUtils.readFileToString( new File( directories[0] + urlBean.getSet()+"/"+ urlBean.getFilename()) ) ;
                    
                    String editedContent =  updateAKIF( akifString, ib );
                    if(editedContent!=null){ System.out.println( "****_updated item : " + urlBean.getFilename());}
                    
                    FileUtils.writeStringToFile( new File(directories[0] +"/"+ urlBean.getSet()+"/"+ urlBean.getFilename()) , editedContent);
                }catch(Exception e){e.printStackTrace();}
                //exp_
                
                
					DomainQueueManager.getInstance().getOutputNoHash().add(urlBean);
					urlBean.setResponseCode(-1);
                    code = -1 ;
                    
		}

                return result ;
	}

    //UPDATE AKIF 
    @SuppressWarnings("unchecked")
	public String updateAKIF( String akifString, IdentifierBean ib ) throws Exception
	{
		JSONObject akifObject = (JSONObject) JSONValue.parse( akifString ) ;
		if ( !ib.getIdentifier().equals( ""+(Long)akifObject.get( "identifier" ) ) )
		{
			throw new Exception( "Identifiers mismatch!: " + ib.getIdentifier() + " differs from " + (Long)akifObject.get( "identifier" ) ) ;
		}
		JSONArray expressions0 = (JSONArray) akifObject.get( "expressions" ) ;
		JSONArray expressions1 = new JSONArray() ;
		for ( Object expression : expressions0 )
		{
			JSONObject expression0 = (JSONObject) expression ;
			JSONArray manifestations0 = (JSONArray)expression0.get( "manifestations" ) ;
			JSONArray manifestations1 = new JSONArray() ;
			for ( Object manifestation : manifestations0 )
			{
				JSONObject manifestation0 = (JSONObject)manifestation ;
				JSONArray items0 = (JSONArray)manifestation0.get( "items" ) ;
				JSONArray items1 = new JSONArray() ;
				for ( Object item : items0 )
				{
					JSONObject item0 = (JSONObject)item ;
					String url = (String)item0.get( "url" ) ;
					// item0.put( "broken", new Boolean( true ) ) ;
					item0.put( "broken", new Boolean( ib.getUrlBeans().get( url ).isBroken() ) ) ;
					items1.add( item0 ) ;
				}
				manifestation0.put( "items", items1 ) ;
				manifestations1.add( manifestation0 ) ;
			}
			expression0.put( "manifestations", manifestations1 ) ;
			expressions1.add( expression0 ) ;
		}
		akifObject.put( "expressions", expressions1 ) ;
		akifObject.put( "lastUpdateDate", utcNow() ) ;
		return akifObject.toJSONString() ;
	}
	
	private String utcNow()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		return sdf.format(cal.getTime());
	}
    
	public Boolean call() throws Exception
	{
		return isUrlBroken() ;
	}
    
    
    
	//NOTE:directories array stores the url for the repository@[0], the broken_items_directory@[1] and the ok_items_directory@[2]
    public void copyAfterTestToFolder(URLBean ubean, boolean broken, String[] directories)
    {
        //--read .properties file
        //             Properties prop = new Properties();
        //             try {//load a properties file
        //             	InputStream propertiesStream = new FileInputStream("config.properties");
        // prop.load(propertiesStream);
        // propertiesStream.close();
        //
        //             } catch (IOException ex) {ex.printStackTrace();}
        
        
        InputStream inStream = null;
        OutputStream outStream = null;
        
        
        
        //IF OK
        File OkDestSubFolder = new File(directories[2] + ubean.getSet());
        if(!OkDestSubFolder.exists())
        {
            System.out.println("New subfolder created!");
            OkDestSubFolder.mkdir();
        }else {
            //System.out.println("Error in creating new subfolder!");
        }
        
        
        File src = new File(directories[0] + ubean.getSet()+"/"+ ubean.getFilename());
        File dest = new File(directories[2] + ubean.getSet()+"/"+ ubean.getFilename());
        
        //IF BROKEN
        if(broken)
        {
            File BrokenDestSubFolder = new File(directories[1] + ubean.getSet());
            if(!BrokenDestSubFolder.exists())
            {
                System.out.println("New subfolder created!");
                BrokenDestSubFolder.mkdir();
            }else {
                //System.out.println("Error in creating new subfolder!");
            }
            
            dest = new File(directories[1] + ubean.getSet()+"/"+ ubean.getFilename());
        }
        
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
        {e.printStackTrace();}
        
        
    }

    
}