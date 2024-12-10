package com.cscodetech.movers.ui;

import static com.cscodetech.movers.utils.SessionManager.currency;
import static com.cscodetech.movers.utils.SessionManager.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.PaymentAdapter;
import com.cscodetech.movers.model.Payment;
import com.cscodetech.movers.model.PaymentdataItem;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.bkash.BkashPaymenrActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PaymentActivity extends BaseActivity implements PaymentAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_mywallet)
    public TextView txtMywallet;
    @BindView(R.id.swichtwallet)
    public Switch swichtwallet;
    @BindView(R.id.recyclerView_payment)
    public RecyclerView recyclerViewPayment;

    @BindView(R.id.lvl_paymentinfo)
    public LinearLayout lvlPaymentinfo;


    @BindView(R.id.txt_subtotal)
    public TextView txtSubtotal;
    @BindView(R.id.lvl_sub_total_row)
    public LinearLayout lvlSubTotalRow;
    @BindView(R.id.lvl_wallet)
    public LinearLayout lvlWallet;
    @BindView(R.id.txt_tax)
    public TextView txtTax;
    @BindView(R.id.lvl_tax_row)
    public LinearLayout lvlTaxRow;
    @BindView(R.id.txt_wallet)
    public TextView txtWallet;
    @BindView(R.id.lvl_wallet_row)
    public LinearLayout lvlWalletRow;
    @BindView(R.id.txt_tpayment)
    public TextView txtTpayment;
    @BindView(R.id.lvl_tpayment_row)
    public LinearLayout lvlTpaymentRow;
    @BindView(R.id.btn_continus)
    public TextView btnContinus;

    UserLogin userLogin;
    CustPrograssbar custPrograssbar;
    List<PaymentdataItem> paymentList = new ArrayList<>();
    String bidAmount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();

        userLogin = sessionmanager.getUserDetails();

        bidAmount = getIntent().getStringExtra("tAmount");

        txtTpayment.setText(sessionmanager.getStringData(currency) + bidAmount);
        txtSubtotal.setText(sessionmanager.getStringData(currency) + bidAmount);
        btnContinus.setText(getResources().getString(R.string.continues) +" "+ sessionmanager.getStringData(currency)+bidAmount);


        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPayment.setItemAnimator(new DefaultItemAnimator());
        getPaymentList();

        if (sessionmanager.getFloatData(wallet) == 0) {
            lvlWalletRow.setVisibility(View.GONE);
            lvlWallet.setVisibility(View.GONE);
        }else {
            txtMywallet.setText(getResources().getString(R.string.balance) + sessionmanager.getStringData(currency) + sessionmanager.getFloatData(wallet));
        }
        Utility.getInstance().payAmount=Double.parseDouble(bidAmount);


        swichtwallet.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (sessionmanager.getFloatData(wallet) < Float.parseFloat(bidAmount)) {
                    double t = Double.parseDouble(bidAmount) - sessionmanager.getFloatData(wallet);
                    txtMywallet.setText(getResources().getString(R.string.balance) +" "+ sessionmanager.getStringData(currency) + "0");
                    Utility.getInstance().tWallet = sessionmanager.getFloatData(wallet);
                    lvlPaymentinfo.setVisibility(View.VISIBLE);
                    txtWallet.setText(sessionmanager.getStringData(currency) + Utility.getInstance().tWallet);
                    txtTpayment.setText(sessionmanager.getStringData(currency)+t);
                    btnContinus.setText(getResources().getString(R.string.continues) +" "+ sessionmanager.getStringData(currency)+t);
                    Utility.getInstance().payAmount=t;
                } else {
                    double t = sessionmanager.getFloatData(wallet) - Double.parseDouble(bidAmount);
                    txtMywallet.setText(getResources().getString(R.string.balance) +" "+ sessionmanager.getStringData(currency) + t);
                    txtTpayment.setText(sessionmanager.getStringData(currency) + "0");
                    btnContinus.setText(getResources().getString(R.string.continues) + " With wallet");

                    Utility.getInstance().tWallet = Double.parseDouble(bidAmount);
                    lvlPaymentinfo.setVisibility(View.GONE);
                    txtWallet.setText(sessionmanager.getStringData(currency) + Utility.getInstance().tWallet);
                    Utility.getInstance().payAmount=0.0;
                }
            } else {
                txtTpayment.setText(sessionmanager.getStringData(currency) + Double.parseDouble(bidAmount));
                txtMywallet.setText(getResources().getString(R.string.balance) +" "+ sessionmanager.getStringData(currency) + sessionmanager.getFloatData(wallet));
                lvlPaymentinfo.setVisibility(View.VISIBLE);
                Utility.getInstance().tWallet = 0;
                txtWallet.setText(sessionmanager.getStringData(currency) + Utility.getInstance().tWallet);
                btnContinus.setText(getResources().getString(R.string.continues) +" "+ sessionmanager.getStringData(currency)+bidAmount);
                Utility.getInstance().payAmount=Double.parseDouble(bidAmount);
            }
        });

    }

    private void getPaymentList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());


            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().paymentgateway(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    String amount="amount";
    String detail="detail";
    @OnClick({R.id.img_back, R.id.btn_continus})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_continus:

                if (Utility.getInstance().payAmount!=0) {

                    switch (paymentItemr.getTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(Utility.getInstance().payAmount);
                            startActivity(new Intent(PaymentActivity.this, RazerpayActivity.class).putExtra(amount, temtoal).putExtra(detail, paymentItemr));
                            finish();
                            break;
                        case "Cash On Delivery":


                            break;
                        case "Paypal":
                            startActivity(new Intent(PaymentActivity.this, PaypalActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItemr));
                            finish();
                            break;
                        case "Stripe":
                            startActivity(new Intent(PaymentActivity.this, StripPaymentActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItemr));
                            finish();
                            break;
                        case "FlutterWave":
                            startActivity(new Intent(PaymentActivity.this, FlutterwaveActivity.class).putExtra(amount, Utility.getInstance().payAmount));
                            finish();
                            break;
                        case "Paytm":
                            startActivity(new Intent(PaymentActivity.this, PaytmActivity.class).putExtra(amount, Utility.getInstance().payAmount));
                            finish();
                            break;
                        case "SenangPay":
                            startActivity(new Intent(PaymentActivity.this, SenangpayActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItemr));
                            finish();
                            break;
                        case "PayStack":
                            int temtoal1 = (int) Math.round(Utility.getInstance().payAmount);
                            startActivity(new Intent(PaymentActivity.this, PaystackActivity.class).putExtra(amount, temtoal1).putExtra(detail, paymentItemr));
                            finish();
                            break;

                        case "SSLCOMMERZ":
                            startActivity(new Intent(PaymentActivity.this, SSLCommerzActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItemr));
                            finish();

                            break;
                        case "Bkash":
                            startActivity(new Intent(PaymentActivity.this, BkashPaymenrActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItemr));
                            finish();

                            break;
                        default:

                            break;
                    }
                } else {
                    Utility.getInstance().paymentsucsses=1;
                    Utility.getInstance().paymentMID="3";
                    Utility.getInstance().tragectionID="0";
                     finish();
                }

                break;
            default:
                break;

        }
    }

    PaymentdataItem paymentItemr;

    @Override
    public void onClickPaymentInfo(PaymentdataItem paymentItem, int position) {

        this.paymentItemr = paymentItem;
        Utility.getInstance().paymentMID = paymentItem.getId();


    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                if (payment.getResult().equalsIgnoreCase("true")) {
                    paymentList = new ArrayList<>();
                    for (int i = 0; i < payment.getPaymentdata().size(); i++) {
                        if (payment.getPaymentdata().get(i).getPShow().equalsIgnoreCase("1")) {
                            paymentList.add(payment.getPaymentdata().get(i));
                        }
                    }

                    PaymentAdapter paymentAdapter = new PaymentAdapter(this, paymentList, this);
                    recyclerViewPayment.setAdapter(paymentAdapter);

                }
            }
        } catch (Exception e) {
            Log.e("Error","-"+e.getMessage());

        }
    }


}