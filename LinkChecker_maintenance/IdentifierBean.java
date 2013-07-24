import java.util.ArrayList;
import java.util.HashMap;
import java.util.List ;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

//import net.zettadata.linkchecker.LinkCheckerException;

public class IdentifierBean 
{
   private String identifier ;
   private String set ;
   private Map<String, URLBean> urlBeans ;
   
   public IdentifierBean( URLBean urlBean )
   {
    this.identifier = urlBean.getIdentifier() ;
    this.set = urlBean.getSet() ;
    urlBeans = new HashMap<String,URLBean>() ;
    urlBeans.put(urlBean.getUrl().toString(), urlBean) ;
   }
   
   public IdentifierBean( String identifier, String set )
   {
    this.identifier = identifier ;
    this.set = set ;
    urlBeans = new HashMap<String,URLBean>() ;
   }
   

   public String getIdentifier() 
   {
    return identifier;
   }

   public void setIdentifier( String identifier ) 
   {
    this.identifier = identifier ;
   }

   public String getSet() 
   {
    return set;
   }

   public void setSet( String set ) 
   {
    this.set = set;
   }
   
   public void addURLBean( URLBean urlBean ) throws Exception//LinkCheckerException
   {
    if ( !identifier.equals( urlBean.getIdentifier() ) ) //Integer.getInteger( urlBean.getIdentifier() )
    {
       throw new Exception( "Identifier mismatch! " 
            + urlBean.getIdentifier() 
            + " does not match: " 
            + identifier ) ;
    }
       
       //FIX THIS EXCEPTION - throws exception even if set and urlBean.getSet equals! different types? (checked once)!@$@#%!@$@#%!@$@#%
//    if ( set.equals( urlBean.getSet() ) )
//    {
//       throw new Exception( "Set mismatch! " 
//            + urlBean.getSet() 
//            + " does not match: " 
//            + set ) ;
//    }
       
    urlBeans.put( urlBean.getUrl().toString(), urlBean ) ;
       
   }
   
   public boolean isEmpty()
   {
    return urlBeans.isEmpty() ;
   }
   
   public int size()
   {
    return urlBeans.size() ;
   }
   
   public Map<String,URLBean> getUrlBeans()
   {
    return urlBeans ;
   }
   
}