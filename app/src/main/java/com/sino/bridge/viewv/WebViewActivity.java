package com.sino.bridge.viewv;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    private static final String LOG_TAG = "WebViewDemo";
    private WebView mWebView;
    private Handler mHandler = new Handler();
    Service service;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
////        webSettings.setSavePassword(false);
//        webSettings.setSaveFormData(false);
        try {
            webSettings.setJavaScriptEnabled(true);
        } catch (Exception e) {
            mWebView.loadUrl("file:///android_asset/demo.html");
        }
////        mWebView.setWebContentsDebuggingEnabled(true);
//        webSettings.setSupportZoom(false);
//        mWebView.setWebChromeClient(new MyWebChromeClient());
//        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
//        mWebView.loadUrl("file:///android_asset/demo.html");
    }

    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void clickOnAndroid() {
            mHandler.post(new Runnable() {
                public void run() {
                    mWebView.loadUrl("javascript:wave()");
                }
            });
        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(LOG_TAG, message);
            result.confirm();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(null);
        if(mWebView!=null){
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.destroy();
            mWebView=null;
        }
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onDestroy();
    }
}
