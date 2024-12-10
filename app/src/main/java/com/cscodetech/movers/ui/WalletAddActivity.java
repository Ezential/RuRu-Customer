package com.cscodetech.movers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.PaymentAdapter;
import com.cscodetech.movers.model.Payment;
import com.cscodetech.movers.model.PaymentdataItem;
import com.cscodetech.movers.model.RestResponse;
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

public class WalletAddActivity extends BaseActivity implements GetResult.MyListener, PaymentAdapter.RecyclerTouchListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_actionbar)
    public TextView txtActionbar;
    @BindView(R.id.ed_wallet)
    public EditText edWallet;
    @BindView(R.id.txt_100)
    public TextView txt100;
    @BindView(R.id.txt_200)
    public TextView txt200;
    @BindView(R.id.txt_300)
    public TextView txt300;
    @BindView(R.id.txt_400)
    public TextView txt400;
    @BindView(R.id.recyclerView_payment)
    public RecyclerView recyclerViewPayment;
    @BindView(R.id.btn_next)
    public TextView btnNext;


    UserLogin userLogin;
    CustPrograssbar custPrograssbar;
    List<PaymentdataItem> paymentList = new ArrayList<>();
    String amount = "amount";
    String detail = "detail";
    public static WalletAddActivity activity;
    public static WalletAddActivity getInstance(){
        return activity;
    }
    public boolean walletUp=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_add);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        activity=this;
        userLogin = sessionmanager.getUserDetails();
        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPayment.setItemAnimator(new DefaultItemAnimator());
        getPaymentList();

    }

    private void getPaymentList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());


            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().paymentgateway(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void paymentCall() {
        try {
            if (!TextUtils.isEmpty(edWallet.getText().toString())) {
                double totalprice = Double.parseDouble(edWallet.getText().toString());
                switch (paymentItem.getTitle()) {
                    case "Razorpay":
                        int temtoal = (int) Math.round(totalprice);

                        startActivity(new Intent(WalletAddActivity.this, RazerpayActivity.class).putExtra(amount, temtoal).putExtra(detail, paymentItem));
                        break;
                    case "Cash On Delivery":


                        break;
                    case "Paypal":

                        startActivity(new Intent(WalletAddActivity.this, PaypalActivity.class).putExtra(amount, totalprice).putExtra(detail, paymentItem));
                        break;
                    case "Stripe":

                        startActivity(new Intent(WalletAddActivity.this, StripPaymentActivity.class).putExtra(amount, totalprice).putExtra(detail, paymentItem));
                        break;
                    case "Flutterwave":

                        startActivity(new Intent(WalletAddActivity.this, FlutterwaveActivity.class).putExtra(amount, totalprice));
                        break;
                    case "Paytm":

                        startActivity(new Intent(WalletAddActivity.this, PaytmActivity.class).putExtra(amount, totalprice));
                        break;
                    case "SenangPay":

                        startActivity(new Intent(WalletAddActivity.this, SenangpayActivity.class).putExtra(amount, totalprice).putExtra(detail, paymentItem));
                        break;
                    case "Paystack":
                        int temtoal1 = (int) Math.round(totalprice);

                        startActivity(new Intent(WalletAddActivity.this, PaystackActivity.class).putExtra(amount, temtoal1).putExtra(detail, paymentItem));
                        break;

                    case "SSLCOMMERZ":

                        startActivity(new Intent(WalletAddActivity.this, SSLCommerzActivity.class).putExtra(amount, totalprice).putExtra(detail, paymentItem));

                        break;
                    case "Bkash":

                        startActivity(new Intent(WalletAddActivity.this, BkashPaymenrActivity.class).putExtra(amount, Utility.getInstance().payAmount).putExtra(detail, paymentItem));


                        break;
                    default:
                        break;
                }
            } else {

                edWallet.setError("");

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "--" + e.getMessage());
        }
    }

    private void addAmount() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());
            jsonObject.put("wallet", edWallet.getText().toString());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().walletUp(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.img_back, R.id.btn_next, R.id.txt_100, R.id.txt_200, R.id.txt_300, R.id.txt_400})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_next:
                if (paymentItem != null && paymentItem.getTitle() != null) {
                    paymentCall();
                } else {
                    Toast.makeText(this, "Select Payment option", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txt_100:
                edWallet.setText("100");
                break;
            case R.id.txt_200:
                edWallet.setText("200");

                break;
            case R.id.txt_300:
                edWallet.setText("300");

                break;
            case R.id.txt_400:
                edWallet.setText("400");

                break;

            default:
                break;

        }
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
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                walletUp = true;
                if (response.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "--> " + e.getMessage());

        }
    }

    PaymentdataItem paymentItem;

    @Override
    public void onClickPaymentInfo(PaymentdataItem pItem, int position) {
        paymentItem = pItem;


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utility.getInstance().paymentsucsses == 1) {
            Utility.getInstance().paymentsucsses = 0;

            addAmount();


        }
    }
}