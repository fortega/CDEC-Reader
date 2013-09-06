/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ssltest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author felipe
 */
public class SSLtest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            HttpsClient cl = new HttpsClient("www.cdec-sic.cl", 443);
            String t;
            
            t = cl.postURL("/redcdec/login.php",
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 31\n\n" +
                    "username=smarta&password=smarta");
            
            t = cl.getURL("/redcdec/explorer.php", null);
            
            Pattern links = Pattern.compile("<a href=\"\\?dir=[A-z0-9/]+\">");
            Matcher m = links.matcher(t);
            while(m.find()){
                System.out.println(m.group());
            }
            
            //System.out.println(t);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
