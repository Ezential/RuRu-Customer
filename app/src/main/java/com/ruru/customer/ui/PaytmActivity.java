package com.ruru.customer.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.ruru.customer.R;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.utils.WebViewClientImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class PaytmActivity extends BaseActivity {
    double amount = 0;

    UserLogin user;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutterwave);

        user = sessionmanager.getUserDetails();
        webView = findViewById(R.id.webview);
        amount = getIntent().getDoubleExtra("amount", 0);
        String postData = null;
        try {
            postData = "amt=" + URLEncoder.encode(String.valueOf(amount))
                    + "&uid=" + URLEncoder.encode(String.valueOf(user.getId()), "UTF-8")
                    + "&mobile=" + URLEncoder.encode(String.valueOf(user.getMobile()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = APIClient.BASE_URL + "/paytm/index.php?" + postData;
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClientImpl webViewClient = new WebViewClientImpl(PaytmActivity.this,webView);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);


    }


}