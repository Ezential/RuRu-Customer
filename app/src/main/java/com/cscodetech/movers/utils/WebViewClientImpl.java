package com.cscodetech.movers.utils;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cscodetech.movers.model.SPayment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebViewClientImpl extends WebViewClient {

    Activity activity;
    WebView webView;

    public WebViewClientImpl(Activity activity,WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.indexOf("jenkov.com") > -1) return false;
        webView.loadUrl(url);
        return true;
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.e("url", "--->" + url);

        if (url.contains("transaction_id")) {
            URL yahoo = getUrl(url);
            if (yahoo != null) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(yahoo.openStream()))) {
                    processInput(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private URL getUrl(String url) {
        URL yahoo = null;
        try {
            yahoo = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return yahoo;
    }

    private void processInput(BufferedReader in) throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Gson gson = new Gson();
            SPayment sPayment = gson.fromJson(inputLine, SPayment.class);
            processSPayment(sPayment);
        }
    }

    private void processSPayment(SPayment sPayment) {
        if (sPayment.getResult().equalsIgnoreCase("true")) {
            Utility.getInstance().tragectionID = sPayment.getTransactionId();
            Utility.getInstance().paymentsucsses = 1;
        } else {
            Utility.getInstance().paymentsucsses = 0;
        }
        Toast.makeText(activity, sPayment.getResponseMsg(), Toast.LENGTH_LONG).show();
        activity.finish();
    }

}