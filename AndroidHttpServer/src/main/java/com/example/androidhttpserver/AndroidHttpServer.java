package com.example.androidhttpserver;


import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.http.Cookie;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;
import com.example.androidhttpserver.servlet.impl.AndroidServletRequestImpl;
import com.example.androidhttpserver.servlet.impl.AndroidServletResponseImpl;
import com.example.androidhttpserver.webinfo.WebMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class AndroidHttpServer extends NanoHTTPD{
    private static final String TAG = "AndroidHttpServer";

    private Context ctx;
    private AssetManager assetManager;

    public AndroidHttpServer(int port, Context ctx) {
        super(port);
        this.ctx = ctx;
        init();
    }

    private void init(){
        assetManager = ctx.getAssets();
    }

    @Override
    public Response serve(String uri,
                          Method method,
                          Map<String, String> headers,
                          Map<String, String> parms,
                          Map<String, String> files) {

        WebMapping mapping = WebMappingSet.findMapping(uri);
        Log.e(TAG, "serve: "+mapping.getServletClass()+" -- "+mapping.getHtmlPath()+" -- "+mapping.getUrl_pattern()+" -- "+uri);
        if(!TextUtils.isEmpty(mapping.getHtmlPath())){
            // html
            String htmlPath = mapping.getHtmlPath();
            return handleAsHtml(htmlPath);
        }else{
            // servlet
            Class<? extends AndroidHttpServlet> servletClass = mapping.getServletClass();
            try {
                //
                // ------ cookie;
                //

                AndroidHttpServlet androidHttpServlet = servletClass.newInstance();
                androidHttpServlet.injectContext(ctx);
                IAndroidServletRequest request = createRequest(uri, method, headers, parms, files);
                AndroidServletResponseImpl response = new AndroidServletResponseImpl();
                androidHttpServlet.doRequest(request,response);

                Response response1 = new Response(response.getStatus(), response.getMimeType(), response.toResponseString());
                Map<String, String> respHeader = response.getHeaders();
                for (Map.Entry<String, String> entry : respHeader.entrySet()) {
                    response1.addHeader(entry.getKey(),entry.getValue());
                }

                List<Cookie> cookies = response.getCookies();
                for (Cookie cookie : cookies) {
                    response1.addHeader("Set-Cookie",cookie.getName()+"="+cookie.getValue());
                }
                return response1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.serve(uri, method, headers, parms, files);
    }

    private Response handleAsHtml(String htmlPath){
        try {

            InputStream in = assetManager.open(htmlPath, AssetManager.ACCESS_BUFFER);

            byte[] buffer = new byte[1024 * 1024];

            int temp = 0;
            int len = 0;
            while((temp=in.read())!=-1){
                buffer[len]=(byte)temp;
                len++;
            }
            in.close();
            return new NanoHTTPD.Response(new String(buffer,0,len));
        } catch (IOException ignored) {
        }
        return handleAsHtml(WebMappingSet.findMapping(WebMappingSet._404).getHtmlPath());
    }

    private IAndroidServletRequest createRequest(String uri,
                                                 Method method,
                                                 Map<String, String> headers,
                                                 Map<String, String> parms,
                                                 Map<String, String> files){
        AndroidServletRequestImpl request = new AndroidServletRequestImpl();

        if(headers != null){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.injectHeader(entry.getKey(),entry.getValue());
            }
        }

        if(parms != null){
            for (Map.Entry<String, String> entry : parms.entrySet()) {
                request.injectParamter(entry.getKey(),entry.getValue());
            }
        }

        request.injectReqUri(uri);
        request.injectReqMethod(method.name());

        return request;
    }
}
