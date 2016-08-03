package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class YYHttpRequest {
    public interface onResponseListener {
        void onResponse( String data );
    }

    private static int next_request_id = -1;
    private static Map<Integer, onResponseListener> request_list = new HashMap<Integer, onResponseListener>();

    private static Handler http_handler = new Handler() {
        public void handleMessage( Message msg ) {
            int request_id = msg.arg1;

            onResponseListener rsp_listener = request_list.get( request_id );
            if( rsp_listener != null ) {
                rsp_listener.onResponse( (String)msg.obj );

                request_list.remove( request_id );
            }
        }
    };

    public static void sendGetRequest( final String URL, final Map<String, String> data, onResponseListener rsp_listener ) {
        final Integer request_id = ++next_request_id;
        request_list.put( request_id, rsp_listener );

        new Thread( new Runnable() {
            public void run() {
                String url = URL;

                if( data != null ) {
                    boolean is_first = true;
                    for( Map.Entry<String, String> item_entry : data.entrySet() ) {
                        url = url + ( is_first ? "?" : "&" ) + item_entry.getKey() + "=" + item_entry.getValue();

                        is_first = false;
                    }
                }

                // 生成请求对象
                HttpGet http_get = new HttpGet( url );
                HttpClient http_client = new DefaultHttpClient();

                Message msg = new Message();
                msg.arg1 = request_id;
                msg.obj = null;

                try {
                    HttpResponse response = http_client.execute( http_get );

                    HttpEntity http_entity = response.getEntity();
                    InputStream inputStream = http_entity.getContent();

                    BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );

                    String result = "";
                    String line = null;
                    while( null != ( line = reader.readLine() ) ) {
                        result += line;
                    }

                    msg.obj = result;
                } catch( Exception e ) {
                    e.printStackTrace();
                }

                http_handler.sendMessage( msg );
            }
        }).start();
    }

    public static void sendPostRequest( final String URL, final Map<String,String> data, onResponseListener rsp_listener ) {
        final Integer request_id = ++next_request_id;
        request_list.put( request_id, rsp_listener );

        new Thread( new Runnable() {
            public void run() {
                List<NameValuePair> data_pairs = new ArrayList<NameValuePair>();

                if( data != null ) {
                    for( Map.Entry<String, String> item_entry : data.entrySet()) {
                        data_pairs.add( new BasicNameValuePair( item_entry.getKey(), item_entry.getValue() ) );
                    }
                }

                Message msg = new Message();
                msg.arg1 = request_id;
                msg.obj = null;

                try {
                    HttpPost http_post = new HttpPost( URL );
                    http_post.setEntity( new UrlEncodedFormEntity( data_pairs ) );

                    HttpClient http_client = new DefaultHttpClient();
                    HttpResponse response = http_client.execute( http_post );

                    HttpEntity http_entity = response.getEntity();
                    InputStream inputStream = http_entity.getContent();

                    BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );

                    String result = "";
                    String line = null;
                    while( null != ( line = reader.readLine() ) ) {
                        result += line;
                    }

                    msg.obj = result;
                } catch( Exception e ) {
                    e.printStackTrace();
                }

                http_handler.sendMessage( msg );
            }
        }).start();
    }
}
