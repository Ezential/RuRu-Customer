package com.ruru.customer.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.ruru.customer.R;
import com.ruru.customer.model.PaymentdataItem;
import com.ruru.customer.utils.SessionManager;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StripPaymentActivity extends BaseActivity {

    @BindView(R.id.txt_total)
    TextView txtTotal;


    PaymentdataItem paymentItem;
    double amount = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strippayment);
        ButterKnife.bind(this);


        paymentItem = (PaymentdataItem) getIntent().getSerializableExtra("detail");
        amount = getIntent().getDoubleExtra("amount", 0.0);
        txtTotal.setText("Total Amount : " + sessionmanager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(amount));


    }


}