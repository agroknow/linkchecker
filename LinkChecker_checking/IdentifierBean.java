import java.util.ArrayList;
import java.util.HashMap;
import java.util.List ;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.zettadata.*;

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
   
   public void addURLBean( URLBean urlBean )  //throws LinkCheckerException
   {
    if ( !identifier.equals( Integer.getInteger( urlBean.getIdentifier() ) ) )
    {
//       throw new LinkCheckerException( "Identifier mismatch! " 
//            + urlBean.getIdentifier()
//            + " does not match: " 
//            + identifier ) ;
        System.out.println( "Identifier mismatch! "
                                       + urlBean.getIdentifier()
                                       + " does not match: "
                                       + identifier ) ;
    }
    if ( set.equals( urlBean.getSet() ) )
    {
//       throw new LinkCheckerException( "Set mismatch! " 
//            + urlBean.getSet() 
//            + " does not match: " 
//            + set ) ;
        System.out.println( "Set mismatch! "
                                       + urlBean.getSet()
                                       + " does not match: "
                                       + set ) ;

    }
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