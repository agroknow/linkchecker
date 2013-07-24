/*
 * @author Mathioudakis Theodore
 */



import java.net.URL;

/**
 *
 * This class describes the URLBeans that created from LOM meta-data
 * this class needs to be comparable in order to be added in a queue
 */


public class URLBean implements Comparable<URLBean>
{
    private URL url;
    private boolean Broken = true;
    private int responseCode = 23; //my lucky number. change it!
    private String filename;
    private String set;
    private String identifier;

    public URLBean(URL url, String filename, String set, String identifier) {
        this.url = url;
        this.set = set;
        this.identifier = identifier;
        this.filename = filename;
    }

    public URLBean(){
    }



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isBroken() {
        return Broken;
    }



    public void setBroken(boolean Broken) {
        this.Broken = Broken;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    

    //this class needs to be comparable in order to be added in a queue
    //UPDATE IT AFTER FINISH THE IMPLEMENTATION
    public int compareTo(URLBean t) throws ClassCastException
    {
        final int DUPLICATE = -1;
        final int UNIQUE = 1;

        if(this.url.equals(t.url) && this.set.equals(t.set) && this.identifier.equals(t.identifier))
        {return DUPLICATE;}

        System.out.println("");
        return UNIQUE;
    }




}
