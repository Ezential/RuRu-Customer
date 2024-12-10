package com.cscodetech.movers.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.utils.WebViewClientImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;


public class SenangpayActivity extends BaseActivity {
    double amount = 0;

    UserLogin user;
    WebView webView;
    Random r;
    String UTF_8_ENCODING = "UTF-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senangpay);

        user = sessionmanager.getUserDetails();
        webView = findViewById(R.id.webview);
        amount = getIntent().getDoubleExtra("amount", 0);
        r= new Random();
        int randomNo = r.nextInt(1000 + 1);
        String postData = null;
        try {
            postData = "detail=" + URLEncoder.encode(getString(R.string.app_name), UTF_8_ENCODING)
                    + "&amount=" + URLEncoder.encode(String.valueOf(amount), UTF_8_ENCODING)
                    + "&order_id=" + URLEncoder.encode(String.valueOf(randomNo), UTF_8_ENCODING)
                    + "&name=" + URLEncoder.encode(user.getName(), UTF_8_ENCODING)
                    + "&email=" + URLEncoder.encode(user.getEmail(), UTF_8_ENCODING)
                    + "&phone=" + URLEncoder.encode(user.getMobile(), UTF_8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = APIClient.BASE_URL +"/result.php?" + postData;
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClientImpl webViewClient = new WebViewClientImpl(SenangpayActivity.this,webView);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);


    }


}