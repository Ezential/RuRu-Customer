package com.ruru.customer.ui.bkash;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.ruru.customer.ui.HomeActivity;

public class BkashJavaScriptInterface {
    Context mContext;
    /**
     * Instantiate the interface and set the context
     */
    public BkashJavaScriptInterface(Context c) {
        mContext = c;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void OnPaymentSuccess(String data) {


        String[] paymentData = data.split("&");
        String transactionID = paymentData[1].trim().replace("TransactionID= ", "").trim();
        String amount = paymentData[2].trim().replace("Amount= ", "").trim();

        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra("TRANSACTION_ID", transactionID);
        intent.putExtra("PAID_AMOUNT", amount);
        intent.putExtra("PAYMENT_SERIALIZE", data);
        mContext.startActivity(intent);
    }

}
