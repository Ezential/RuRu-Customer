package com.ruru.customer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ruru.customer.R;
import com.ruru.customer.model.PaymentdataItem;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.utils.Utility;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import butterknife.ButterKnife;


public class RazerpayActivity extends BaseActivity implements PaymentResultListener {

    double amount = 0;
    UserLogin user;
    PaymentdataItem paymentItem;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_razorpay);
        ButterKnife.bind(this);

        user = sessionmanager.getUserDetails();
        amount = getIntent().getIntExtra("amount", 0);
        paymentItem = (PaymentdataItem) getIntent().getSerializableExtra("detail");
        startPayment(String.valueOf(amount));
    }

    public void startPayment(String amount) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(paymentItem.getAttributes());
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("currency", "INR");
            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", user.getMobile());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentSuccess(String s) {

        Utility.getInstance().tragectionID = s;
        Utility.getInstance().paymentsucsses = 1;
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        finish();
    }
}