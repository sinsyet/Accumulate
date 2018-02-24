package com.example.androidhttpserver;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class SimpleServer extends NanoHTTPD {
	private static final String TAG = "SimpleServer";
	AssetManager asset_mgr;
	
    public SimpleServer() {
        super(9999);
    }

    public Response serve(String uri, Method method, 
            Map<String, String> header,
            Map<String, String> parameters,
            Map<String, String> files)
    {
		Log.e(TAG, "serve: "+uri);
		int len = 0;
		byte[] buffer = null;
		Log.d("jltxgcy", header.get("remote-addr"));
    	
    	String file_name = uri.substring(1);
		Log.e(TAG, "serve: "+file_name);
		if(file_name.equalsIgnoreCase("")){
    		file_name = "html/index.html";
    	}else if(file_name.equalsIgnoreCase("login")){
			String usn = parameters.get("usn");
			String psw = parameters.get("psw");
			if("admin".equals(usn) && "123456".equals(psw)){
				file_name = "html/login_s.html";
			}else{
				file_name = "html/login_f.html";
			}
		}

    	try {
			
			InputStream in = asset_mgr.open(file_name, AssetManager.ACCESS_BUFFER);
			
		 	buffer = new byte[1024*1024];
	        
		 	int temp=0;
	        while((temp=in.read())!=-1){
	        	buffer[len]=(byte)temp;  
	            len++;  
	        }
		    in.close();
        	return new NanoHTTPD.Response(new String(buffer,0,len));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.serve(uri, method, header, parameters, files);
    }
}