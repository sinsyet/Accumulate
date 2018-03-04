package com.example.sample.activities;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sample.R;

/**
 * https://app.pinnenger.com:11443/registerindex.html?to

 ＝privacyClause
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WebViewActivity";
    private static final String URL = "https://app.pinnenger.com:11443/registerindex.html?to=privacyClause";
    private WebView mWv;
    private EditText mEt;
    private View mFail;
    private View mSuccess;
    private View mLoading;

    private int mCurState;
    private TextView mTvFailDesc;

    private Handler mHandler = new Handler();
    private long start;

    interface State{
        int LOADING = 0;
        int FAIL = 1;
        int SUCCESS = 2;
    }

    private boolean isLoadingSuccess;
    private Runnable onDetectRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: "+isLoadingSuccess);
            if(isLoadingSuccess){
                if(isNetActive()) {
                    // nothing to do
                    switchState(State.SUCCESS);
                }else{
                    mTvFailDesc.setText("NET DISABLE");
                    switchState(State.FAIL);
                }
            }else{
                mTvFailDesc.setText("LOADING TIMEOUT");
                switchState(State.FAIL);
            }
        }
    };

    private boolean isNetActive(){
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()) return true;
        }catch (Exception ignored){
        }
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView(){
        mWv = findViewById(R.id.wba_wb);
        findViewById(R.id.wba_btn_enter).setOnClickListener(this);
        findViewById(R.id.wba_btn_refresh).setOnClickListener(this);
        mEt = findViewById(R.id.wba_et);
        mFail = findViewById(R.id.wba_fail);
        mLoading = findViewById(R.id.wba_loading);
        mTvFailDesc = findViewById(R.id.wba_tv_failcontent);
        mSuccess = findViewById(R.id.wba_success);
        mWv.getSettings().setJavaScriptEnabled(true);


        mWv.setPictureListener(new WebView.PictureListener() {
            @Override
            public void onNewPicture(WebView view, Picture picture) {
                Log.e(TAG, "onNewPicture: "+(System.currentTimeMillis()-start));
                isLoadingSuccess = true;

            }
        });

        mWv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isLoadingSuccess = false;
                switchState(State.LOADING);
                Log.e(TAG, "onPageStarted: "+url);
                mHandler.postDelayed(onDetectRunnable,10 * 1000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e(TAG, "onPageFinished: "+url);

            }



            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e(TAG, "onLoadResource: "+url);
                super.onLoadResource(view, url);
                isLoadingSuccess = false;
                start = System.currentTimeMillis();
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "onReceivedError: "+error.getErrorCode());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "onReceivedError: "+errorCode
                        +" description: "+description
                        +"failingUrl: "+failingUrl);
                mHandler.removeCallbacks(onDetectRunnable);
                mTvFailDesc.setText(description);
                switchState(State.FAIL);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e(TAG, "onReceivedHttpError: "+errorResponse.getStatusCode());
            }
        });
    }

    private String mUrl;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wba_btn_enter:
                String url = mEt.getText().toString();
                if(TextUtils.isEmpty(url)){
                    url = URL;
                    mUrl = url;
                    Log.e(TAG, "onClick: "+url);
                    mWv.loadUrl(url);
                }else{
                    if(!url.startsWith("http://") && !url.startsWith("https://")){
                        url = "http://"+url;
                    }
                    mUrl = url;
                    mWv.loadUrl(url);
                }
                break;
            case R.id.wba_btn_refresh:

                mWv.loadUrl(mUrl);
                break;
        }
    }

    private void switchState(int state){
        getStateView(mCurState).setVisibility(View.GONE);
        getStateView(state).setVisibility(View.VISIBLE);
        mCurState = state;
        Log.e(TAG, "switchState: "+state);
    }

    private View getStateView(int state){
        switch(state){
            case State.FAIL: return mFail;
            case State.SUCCESS: return mSuccess;
            default: return mLoading;
        }
    }
}
