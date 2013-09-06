/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ssltest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author felipe
 */
public class HttpsClient {
    private SSLSocket s;
    private InputStream in;
    private OutputStream out;
    private String host;
    private int port;
    private String cookies;
    private Pattern regexCookie = Pattern.compile("^Set-Cookie: (PHPSESSID=[a-z0-9]+)", Pattern.MULTILINE);
    public HttpsClient(String host, int port){
        this.host = host;
        this.port = port;
        this.cookies = "";
    }
    
    private String workURL(String url, String method, String headers) throws IOException{
        s = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, port);
        in = s.getInputStream();
        out = s.getOutputStream();
        
        if(headers != null)
            headers += "\n";
        else
            headers = "";
        
        String msg =
                method + " " + url + " / HTTP/1.1\n" +
                "host: " + host + "\n" +
                "Cookie: " + cookies + "\n" +
                headers +
                "\n";
        
        out.write(toByte(msg));        
        String r = read();
        
        out.close();
        in.close();
        s.close();
        
        setCookies(r);
        return r;
    }
    
    public String postURL(String url, String headers) throws IOException{
        return workURL(url,"POST", headers);
    }
    
    public String getURL(String url, String headers) throws IOException{
        return workURL(url,"GET", headers);
    }
    
    private void setCookies(String t){
        Matcher cookieMatcher = regexCookie.matcher(t);
        if(cookieMatcher.find())
            cookies = cookieMatcher.group(1);
    }
    
    private String read() throws IOException{
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[16];
        int n = 0;
        
        do{
            n = in.read(buffer);
            for(int i = 0; i < n; i++)
                sb.append((char)buffer[i]);
        }while(n > 0);
        
        return sb.toString();
    }
    
    private byte[] toByte(String s){
        byte[] r = new byte[s.length()];
        
        for(int i = 0; i < r.length; i++)
            r[i] = (byte)s.charAt(i);
        
        return r;
    }
}
