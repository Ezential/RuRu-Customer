package com.cscodetech.movers.ui.bkash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.movers.R;
import com.google.gson.Gson;

public class BkashPaymenrActivity extends AppCompatActivity {

    WebView wvBkashPayment;
    ProgressBar sProgressBar;
    Double amount= 0.0;
    String request = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_paymenr);
        wvBkashPayment = findViewById(R.id.wvBkashPayment);
        sProgressBar = findViewById(R.id.progressBar);

        //check there is any intent data or not
        if (getIntent().getExtras() == null) {
            //no data
            Toast.makeText(this, "Amount is empty. You can't pay through bkash. Try again", Toast.LENGTH_SHORT).show();
            return;
        } else {
            amount = getIntent().getExtras().getDouble("amount");  //make sure your keyname is same as MainActivity.

            //Create a PaymentRequests model
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount(String.valueOf(amount));
            paymentRequest.setIntent("sale");

            Gson gson = new Gson();
            request = gson.toJson(paymentRequest);

            WebSettings webSettings = wvBkashPayment.getSettings();
            webSettings.setJavaScriptEnabled(true);

            /*
             * Below part is for enabling webview settings for using javascript and accessing html files and other assets
             */
            wvBkashPayment.setClickable(true);
            wvBkashPayment.getSettings().setDomStorageEnabled(true);
            wvBkashPayment.getSettings().setAppCacheEnabled(false);
            wvBkashPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            wvBkashPayment.clearCache(true);
            wvBkashPayment.getSettings().setAllowFileAccessFromFileURLs(true);
            wvBkashPayment.getSettings().setAllowUniversalAccessFromFileURLs(true);

            /*
             * To control any kind of interaction from html file
             */
            wvBkashPayment.addJavascriptInterface(new BkashJavaScriptInterface(this), "KinYardsPaymentData");

            wvBkashPayment.loadUrl("https://www.hosting.com/api/payment.php");   // api host link .

            wvBkashPayment.setWebViewClient(new CheckoutWebViewClient());
        }
    }
    private class CheckoutWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("External URL: ", url);
            if (url.equals("https://www.bkash.com/terms-and-conditions")) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            throw new UnsupportedOperationException("WebViewClientImpl() is not supported");

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String paymentRequest = "{paymentRequest:" + request + "}";
            wvBkashPayment.loadUrl("javascript:callReconfigure(" + paymentRequest + " )");
            wvBkashPayment.loadUrl("javascript:getAmount(" + amount + " )");
            wvBkashPayment.loadUrl("javascript:clickPayButton()");
            sProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        popupPaymentCancelAlert();  //people can press backBtn and payment may cancel. so alert he really want to cancel payment or not
    }

    private void popupPaymentCancelAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Want to cancel payment process?");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher_background);
        alert.setTitle("Alert!");
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            Toast.makeText(BkashPaymenrActivity.this, "Payment canceled", Toast.LENGTH_SHORT).show();
            BkashPaymenrActivity.super.onBackPressed();
        });

        alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}