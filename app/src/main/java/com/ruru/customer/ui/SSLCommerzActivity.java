package com.ruru.customer.ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ruru.customer.R;
import com.ruru.customer.model.PaymentdataItem;
import com.ruru.customer.model.SSlModel;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.retrofit.GetResult;
import com.ruru.customer.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sslcommerz.library.payment.Classes.PayUsingSSLCommerz;
import com.sslcommerz.library.payment.Listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.Util.ConstantData.CurrencyType;
import com.sslcommerz.library.payment.Util.ConstantData.ErrorKeys;
import com.sslcommerz.library.payment.Util.ConstantData.SdkCategory;
import com.sslcommerz.library.payment.Util.JsonModel.TransactionInfo;
import com.sslcommerz.library.payment.Util.Model.MandatoryFieldModel;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class SSLCommerzActivity extends Activity implements GetResult.MyListener {
    PaymentdataItem paymentItem;
    String sendbox;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslcommerz);


        paymentItem = (PaymentdataItem) getIntent().getSerializableExtra("detail");
        amount = String.valueOf(getIntent().getDoubleExtra("amount", 0));
        try {

            List<String> elephantList = Arrays.asList(paymentItem.getAttributes().split(","));
            if (elephantList.size() == 3) {
                if (elephantList.get(2).equalsIgnoreCase("TEST")) {
                    sendbox = "TESTBOX";
                } else {
                    sendbox = "LIVE";
                }

            } else {
                finish();
            }

            MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel(elephantList.get(0), elephantList.get(1), amount, System.currentTimeMillis() + "", CurrencyType.USD, sendbox, SdkCategory.BANK_LIST);
            /*Call for the payment*/
            PayUsingSSLCommerz.getInstance().setData(this, mandatoryFieldModel, new OnPaymentResultListener() {
                @Override
                public void transactionSuccess(TransactionInfo transactionInfo) {
                    // If payment is success and risk label is 0 get payment details from here
                    if (transactionInfo.getRiskLevel().equals("0")) {

                        Log.e("transactionInfo ", transactionInfo.getValId());
                        transaction(transactionInfo.getValId());

                    }
// Payment is success but payment is not complete yet. Card on hold now.
                    else {
                        Log.e("TAG", "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle());
                    }
                }

                @Override
                public void transactionFail(TransactionInfo transactionInfo) {
                    Log.e("transactionFail", transactionInfo.getStatus());

                }

                @Override
                public void error(int errorCode) {
                    switch (errorCode) {
                        case ErrorKeys.USER_INPUT_ERROR:
                            Log.e("TAG", "User Input Error");
                            break;
                        case ErrorKeys.INTERNET_CONNECTION_ERROR:
                            Log.e("TAG", "Internet Connection Error");
                            break;
                        case ErrorKeys.DATA_PARSING_ERROR:
                            Log.e("TAG", "Data Parsing Error");
                            break;
                        case ErrorKeys.CANCEL_TRANSACTION_ERROR:
                            Log.e("TAG", "User Cancel The Transaction");
                            break;
                        case ErrorKeys.SERVER_ERROR:
                            Log.e("TAG", "Server Error");
                            break;
                        case ErrorKeys.NETWORK_ERROR:
                            Log.e("TAG", "Network Error");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + errorCode);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ExceptionException", e.toString());
        }

    }

    private void transaction(String trgID) {
        Call<JsonObject> call = APIClient.getTrasection().tragection(trgID, "cscod6242b3f49944d", "cscod6242b3f49944d@ssl");
        GetResult getResult = new GetResult(this);
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            Log.e("resulet", "==>" + result);
            Gson gson = new Gson();
            SSlModel sSlModel = gson.fromJson(result, SSlModel.class);
            if (sSlModel.getStatus().equals("VALIDATED")) {

                Utility.getInstance().tragectionID = sSlModel.getTranId();
                Utility.getInstance().paymentsucsses = 1;
                finish();
            } else {
                Utility.getInstance().paymentsucsses = 0;
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}