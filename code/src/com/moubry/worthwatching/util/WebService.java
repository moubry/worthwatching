package com.moubry.worthwatching.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
 
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
import com.google.gson.Gson;
import com.moubry.worthwatching.R;

public class WebService{
 
	public static final String TAG = "WebService";
	
    DefaultHttpClient httpClient;
    HttpContext localContext;
    private String ret;
 
    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    String webServiceUrl;
 
    //The serviceName should be the name of the Service you are going to be using.
    public WebService(String serviceName){
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        httpClient = new DefaultHttpClient(myParams);
        localContext = new BasicHttpContext();
        webServiceUrl = serviceName;
    }
 
    //Use this method to do a HttpPost\WebInvoke on a Web Service
    public String webInvoke(String methodName, Map<String, Object> params) {
 
        JSONObject jsonObject = new JSONObject();
 
        for (Map.Entry<String, Object> param : params.entrySet()){
            try {
                jsonObject.put(param.getKey(), param.getValue());
            }
            catch (JSONException e) {
                Log.e(TAG, "JSONException in webInvoke");
            }
        }
        return webInvoke(methodName,  jsonObject.toString(), "application/json");
    }
 
    private String webInvoke(String methodName, String data, String contentType) {
        ret = null;
 
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
 
        httpPost = new HttpPost(webServiceUrl + methodName);
        response = null;
 
        StringEntity tmp = null;        
 
        //httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        httpPost.setHeader("Accept",
"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
 
        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
 
        try {
            tmp = new StringEntity(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "HttpUtils Unsupported exception");
        }
 
        httpPost.setEntity(tmp);
 
        Log.d(TAG, webServiceUrl + "?" + data);
 
        try {
            response = httpClient.execute(httpPost,localContext);
 
            if (response != null) {
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtils exception");
        }
 
        return ret;
    }
 
    //Use this method to do a HttpGet/WebGet on the web service
	public String webGet(String methodName, Map<String, String> params) {
        String getUrl = webServiceUrl + methodName;
 
        int i = 0;
        for (Map.Entry<String, String> param : params.entrySet())
        {
            if(i == 0){
                getUrl += "?";
            }
            else{
                getUrl += "&";
            }
 
            try {
                getUrl += param.getKey() + "=" + URLEncoder.encode(param.getValue(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 
            i++;
        }
 
        httpGet = new HttpGet(getUrl);
        Log.e("WebGetURL: ",getUrl);
 
        
        try {
            response = httpClient.execute(httpGet);
        } catch (UnknownHostException e) {
        	Log.e(TAG, "httpClient.execute(httpGet) UnknownHostException ");
        	return null;
        }
       catch (Exception e)
       {
    	   Log.e(TAG, "httpClient.execute(httpGet) exception ");
       }
       
       if (response == null)
    	   return null;
 
        // we assume that the response body contains the error message
        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
        	Log.e(TAG, "response.getEntity() Exception");
        }
 
        return ret;
    }
 
    public static JSONObject Object(Object o){
        try {
            return new JSONObject(new Gson().toJson(o));
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception");
        }
        return null;
    }
 
    public InputStream getHttpStream(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
 
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
 
        if (!(conn instanceof HttpURLConnection))
        {
        	Log.e(TAG, "Not an HTTP connection");
            throw new IOException("Not an HTTP connection");
        }
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 
 
            response = httpConn.getResponseCode();                 
 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
        	Log.e(TAG, "Error Connecting");
            throw new IOException("Error connecting");
        } // end try-catch
 
        return in;
    }
 
    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }
 
    public void abort() {
        try {
            if (httpClient != null) {
                Log.e(TAG,"aborting");
                httpPost.abort();
            }
        } catch (Exception e) {
            Log.e(TAG,"exception aborting");
        }
    }
}