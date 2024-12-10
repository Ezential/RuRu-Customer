package com.ruru.customer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ruru.customer.R;
import com.ruru.customer.model.PaymentdataItem;
import com.ruru.customer.utils.Utility;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class PaypalActivity extends BaseActivity {
    PaymentdataItem paymentItem;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    PayPalConfiguration config;
    double amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        paymentItem = (PaymentdataItem) getIntent().getSerializableExtra("detail");

        amount = getIntent().getDoubleExtra("amount", 0.0);
        String configEnvironment;
        List<String> elephantList = Arrays.asList(paymentItem.getAttributes().split(","));
        if (elephantList.size() == 2) {
            if (elephantList.get(1).equalsIgnoreCase("1")) {
                configEnvironment = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
            } else {
                configEnvironment = PayPalConfiguration.ENVIRONMENT_SANDBOX;
            }
            config = new PayPalConfiguration()
                    .environment(configEnvironment)
                    .clientId(elephantList.get(0))
                    .merchantName(getResources().getString(R.string.app_name));

            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);
            onBuyPressed();
        } else {
            finish();
        }

    }

    public void onBuyPressed() {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(BigDecimal.valueOf(amount), "USD", getResources().getString(R.string.app_name),
                paymentIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PAYMENT:
                handlePaymentResult(resultCode, data);
                break;
            case REQUEST_CODE_FUTURE_PAYMENT:
                handleFuturePaymentResult(resultCode, data);
                break;
            case REQUEST_CODE_PROFILE_SHARING:
                handleProfileSharingResult(resultCode, data);
                break;

            default:
                Log.e("jdja","djjasd");
                break;
        }
    }

    private void handlePaymentResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm =
                    data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    JSONObject jsonDetails = confirm.toJSONObject().getJSONObject("response");
                    Log.e("TAG", jsonDetails.toString(4));
                    Log.e("TAG", confirm.getPayment().toJSONObject().toString(4));
                    showDetails(jsonDetails);
                } catch (JSONException e) {
                    Log.e("TAG", "failure occurred: ", e);
                }
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.e("TAG", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        } else {
            finish();
        }
    }

    private void handleFuturePaymentResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth =
                    data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                displayResultText("Future Payment code received from PayPal");
            }
        } else {
            Log.e("FuturePaymentExample", "The user canceled.");
            finish();
        }
    }

    private void handleProfileSharingResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth =
                    data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                displayResultText("Profile Sharing code received from PayPal");
            }
        } else {
            Log.e("ProfileSharingExample", "The user canceled.");
            finish();
        }
    }


    protected void displayResultText(String result) {
        Toast.makeText(
                        getApplicationContext(),
                        result, Toast.LENGTH_LONG)
                .show();
    }

    private void showDetails(JSONObject jsonDetails) throws JSONException {
        if (jsonDetails.getString("state").equalsIgnoreCase("approved")) {

            Utility.getInstance().paymentsucsses = 1;
            Utility.getInstance().tragectionID = jsonDetails.getString("id");
            finish();
        }
    }
}
