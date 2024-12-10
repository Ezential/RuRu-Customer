package com.cscodetech.movers.ui.findlorry;

import static com.cscodetech.movers.utils.SessionManager.currency;
import static com.cscodetech.movers.utils.SessionManager.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.model.RestResponse;
import com.cscodetech.movers.model.postload.MyLoadData;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.BaseActivity;
import com.cscodetech.movers.ui.PaymentActivity;
import com.cscodetech.movers.ui.postload.BiderInfoActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.SessionManager;
import com.cscodetech.movers.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BookLorryDetailsActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.img_menu)
    public ImageView imgMenu;
    @BindView(R.id.imgt)
    public ImageView imgt;
    @BindView(R.id.txt_title)
    public TextView txtTitle;
    @BindView(R.id.txt_price)
    public TextView txtPrice;
    @BindView(R.id.txt_pricel)
    public TextView txtPricel;
    @BindView(R.id.txt_tonneorfix)
    public TextView txtTonneorfix;

    @BindView(R.id.txt_pick)
    public TextView txtPick;
    @BindView(R.id.txt_pickaddress)
    public TextView txtPickaddress;
    @BindView(R.id.txt_drop)
    public TextView txtDrop;
    @BindView(R.id.txt_dropaddress)
    public TextView txtDropaddress;
    @BindView(R.id.txt_date)
    public TextView txtDate;
    @BindView(R.id.txt_tonne)
    public TextView txtTonne;
    @BindView(R.id.txt_material)
    public TextView txtMaterial;
    @BindView(R.id.imgs)
    public ShapeableImageView imgs;
    @BindView(R.id.txt_name)
    public TextView txtName;
    @BindView(R.id.txt_rates)
    public TextView txtRates;
    @BindView(R.id.txt_status)
    public TextView txtStatus;
    @BindView(R.id.txt_pay)
    public TextView txtPay;
    @BindView(R.id.txt_reject)
    public TextView txtReject;
    @BindView(R.id.txt_accept)
    public TextView txtAccept;
    @BindView(R.id.txt_offer)
    public TextView txtOffer;
    @BindView(R.id.lvl_lorryownerresponse)
    public LinearLayout lvlLorryownerresponse;
    @BindView(R.id.lvl_nobids)
    public LinearLayout lvlNobids;
    @BindView(R.id.txt_pmethod)
    public TextView txtPmethod;
    @BindView(R.id.pmethod_row)
    public LinearLayout pmethodRow;
    @BindView(R.id.txt_trasaction)
    public TextView txtTrasaction;
    @BindView(R.id.lvl_transaction_row)
    public LinearLayout lvlTransactionRow;
    @BindView(R.id.txt_subtotal)
    public TextView txtSubtotal;
    @BindView(R.id.lvl_sub_total_row)
    public LinearLayout lvlSubTotalRow;

    @BindView(R.id.txt_wallet)
    public TextView txtWallet;

    @BindView(R.id.txt_tonneorfixl)
    public TextView txtTonneorfixl;
    @BindView(R.id.txt_totall)
    public TextView txtTotall;
    @BindView(R.id.txt_total)
    public TextView txtTotal;

    @BindView(R.id.btn_rating)
    public TextView btnRating;
    @BindView(R.id.img_call)
    public ImageView imgCall;
    @BindView(R.id.lvl_wallet_row)
    public LinearLayout lvlWalletRow;
    @BindView(R.id.txt_tpayment)
    public TextView txtTpayment;
    @BindView(R.id.lvl_tpayment_row)
    public LinearLayout lvlTpaymentRow;
    @BindView(R.id.lvl_paymentinfo)
    public LinearLayout lvlPaymentinfo;
    @BindView(R.id.txt_actionbar)
    public TextView txtActionbar;
    @BindView(R.id.txt_order_stast)
    public TextView txtOrderStast;

    CustPrograssbar custPrograssbar;
    String loadId = "load_id";
    String status = "status";
    String applicationjson = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_lorry_details);
        ButterKnife.bind(this);

        custPrograssbar = new CustPrograssbar();
        getBookLoadDetails();
    }

    private void getBookLoadDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionmanager.getUserDetails().getId());
            jsonObject.put(loadId, getIntent().getStringExtra("lid"));
            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse(applicationjson));
            Call<JsonObject> call = APIClient.getInterface().bookDetails(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(BookLorryDetailsActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRateUpdate(String rate, String ratetext) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionmanager.getUserDetails().getId());
            jsonObject.put(loadId, getIntent().getStringExtra("lid"));
            jsonObject.put("total_trate", rate);
            jsonObject.put("rate_ttext", ratetext);

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse(applicationjson));
            Call<JsonObject> call = APIClient.getInterface().rateUpdate(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(BookLorryDetailsActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MyLoadData myLoadData;
    String tonne = "Tonne";

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            custPrograssbar.closePrograssBar();

            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                myLoadData = gson.fromJson(result.toString(), MyLoadData.class);
                txtActionbar.setText(getResources().getString(R.string.load) + " #" + myLoadData.getLoadDetails().getId());
                Glide.with(this).load(APIClient.BASE_URL + "/" + myLoadData.getLoadDetails().getVehicleImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgt);
                txtTitle.setText(myLoadData.getLoadDetails().getVehicleTitle());

                if (myLoadData.getLoadDetails().getAmtType().equalsIgnoreCase(tonne)) {
                    txtPrice.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getAmount());
                    txtTonneorfix.setText("/ " + myLoadData.getLoadDetails().getAmtType());
                    txtTotal.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getTotalAmt());

                    if (myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("3") || myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("6")) {
                        txtPricel.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getOfferPrice());
                        txtTonneorfixl.setText(" /" + myLoadData.getLoadDetails().getAmtType());
                        txtTotall.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getOfferTotal());

                    } else {
                        txtPricel.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getAmount());
                        txtTonneorfixl.setText(" /" + myLoadData.getLoadDetails().getAmtType());
                        txtTotall.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getTotalAmt());
                    }
                } else {
                    txtPrice.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getAmount());
                    txtTonneorfix.setText("/ " + myLoadData.getLoadDetails().getAmtType());
                    txtTotal.setVisibility(View.GONE);
                    if (myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("3") || myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("6")) {
                        txtPricel.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getOfferTotal());
                        txtTonneorfixl.setText(" /" + myLoadData.getLoadDetails().getOfferType());

                    } else {
                        txtPricel.setText(sessionmanager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getTotalAmt());
                        txtTonneorfixl.setText(" /" + myLoadData.getLoadDetails().getAmtType());
                    }
                    txtTotall.setVisibility(View.GONE);
                }
                txtPick.setText(myLoadData.getLoadDetails().getPickupState());
                txtPickaddress.setText(myLoadData.getLoadDetails().getPickupPoint());
                txtDrop.setText(myLoadData.getLoadDetails().getDropState());
                txtDropaddress.setText(myLoadData.getLoadDetails().getDropPoint());
                txtDate.setText("" + Utility.getInstance().parseDateToddMMyy(myLoadData.getLoadDetails().getPostDate()));
                txtTonne.setText("" + myLoadData.getLoadDetails().getWeight());
                txtMaterial.setText("" + myLoadData.getLoadDetails().getMaterialName());

                Glide.with(this).load(APIClient.BASE_URL + "/" + myLoadData.getLoadDetails().getBidderImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgs);
                txtName.setText(myLoadData.getLoadDetails().getBidderName());


                txtRates.setText(myLoadData.getLoadDetails().getRate());
                txtOrderStast.setText(myLoadData.getLoadDetails().getMessage());


                switch (myLoadData.getLoadDetails().getFlowId()) {
                    case "0":
                        lvlPaymentinfo.setVisibility(View.GONE);
                        txtStatus.setVisibility(View.VISIBLE);
                        txtStatus.setText("Waiting for response");
                        break;
                    case "1":

                        lvlPaymentinfo.setVisibility(View.GONE);
                        txtStatus.setVisibility(View.GONE);
                        txtPay.setVisibility(View.VISIBLE);
                        imgCall.setVisibility(View.VISIBLE);

                        break;
                    case "2":
                    case "5":
                        lvlPaymentinfo.setVisibility(View.GONE);
                        txtStatus.setVisibility(View.VISIBLE);
                        txtStatus.setText("" + myLoadData.getLoadDetails().getCommentReject());
                        txtPay.setVisibility(View.GONE);
                        break;
                    case "3":
                        lvlPaymentinfo.setVisibility(View.GONE);
                        lvlLorryownerresponse.setVisibility(View.VISIBLE);
                        txtStatus.setVisibility(View.GONE);

                        txtPay.setVisibility(View.GONE);
                        break;
                    case "4":
                    case "7":
                        imgCall.setVisibility(View.VISIBLE);
                        lvlPaymentinfo.setVisibility(View.VISIBLE);
                        txtStatus.setVisibility(View.GONE);
                        txtPay.setVisibility(View.GONE);
                        txtPmethod.setText("" + myLoadData.getLoadDetails().getpMethodName());
                        txtTrasaction.setText("" + myLoadData.getLoadDetails().getOrderTransactionId());
                        txtSubtotal.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getTotalAmt());
                        txtWallet.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getWalAmt());
                        txtTpayment.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getPayAmt());
                        break;
                    case "6":
                        lvlPaymentinfo.setVisibility(View.GONE);
                        txtStatus.setVisibility(View.VISIBLE);
                        txtStatus.setText("Waiting for Offer Response");

                        break;
                    case "8":

                        lvlPaymentinfo.setVisibility(View.VISIBLE);
                        txtStatus.setVisibility(View.GONE);
                        txtPay.setVisibility(View.GONE);
                        txtPmethod.setText("" + myLoadData.getLoadDetails().getpMethodName());
                        txtTrasaction.setText("" + myLoadData.getLoadDetails().getOrderTransactionId());
                        txtSubtotal.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getTotalAmt());
                        txtWallet.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getWalAmt());
                        txtTpayment.setText(sessionmanager.getStringData(currency) + myLoadData.getLoadDetails().getPayAmt());
                        if (myLoadData.getLoadDetails().getIsRate().equalsIgnoreCase("0")) {
                            btnRating.setVisibility(View.VISIBLE);
                            btnRating.setText(getResources().getString(R.string.rate_to) + " " + myLoadData.getLoadDetails().getBidderName());
                        }
                        break;
                    default:

                        break;

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {

                    sessionmanager.setFloatData(wallet, Float.parseFloat(response.getWallet()));
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {

                    finish();
                }
            }

        } catch (Exception e) {
            Log.e("Error", "--> " + e.getMessage());

        }
    }

    private void getDecision(JSONObject jsonObject) {

        RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse(applicationjson));
        Call<JsonObject> call = APIClient.getInterface().offerDecision(bodyRequest);
        GetResult getResult = new GetResult(this);
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
        custPrograssbar.prograssCreate(BookLorryDetailsActivity.this);

    }


    public void bidderOffers(Context context) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.lorry_book_offer_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txtClick = rootView.findViewById(R.id.btn_sendoffer);
        EditText edAmount = rootView.findViewById(R.id.ed_amount);
        EditText edDescription = rootView.findViewById(R.id.ed_description);
        Switch swichtonne = rootView.findViewById(R.id.swichtonne);
        final String[] atype = {""};
        final double[] tamout = new double[1];
        swichtonne.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                if (b) {
                    swichtonne.setText("Per Tonnes");
                    atype[0] = tonne;
                    tamout[0] = Double.parseDouble(edAmount.getText().toString()) * Double.parseDouble(myLoadData.getLoadDetails().getWeight());

                } else {
                    swichtonne.setText("Fix");
                    atype[0] = "Fixed";


                }
            }

        });

        txtClick.setOnClickListener(v -> {
            if (!swichtonne.isChecked()) {
                atype[0] = "Fixed";
                tamout[0] = Double.parseDouble(edAmount.getText().toString());
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", sessionmanager.getUserDetails().getId());
                jsonObject.put(loadId, getIntent().getStringExtra("lid"));
                jsonObject.put(status, "3");
                jsonObject.put("offer_description", edDescription.getText().toString());
                jsonObject.put("offer_price", edAmount.getText().toString());
                jsonObject.put("offer_type", atype[0]);
                jsonObject.put("offer_total", tamout[0]);

                getDecision(jsonObject);

                mBottomSheetDialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        mBottomSheetDialog.show();
    }

    String clickOff = "";

    @OnClick({R.id.img_back, R.id.txt_reject, R.id.btn_rating,R.id.txt_accept, R.id.txt_offer, R.id.txt_pay, R.id.img_call, R.id.imgs})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();


        } else if (id == R.id.btn_rating) {
            sendReview(this);
        } else if (id == R.id.img_call) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + myLoadData.getLoadDetails().getBidderMobile()));
            startActivity(intent);
        } else if (id == R.id.txt_reject) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", sessionmanager.getUserDetails().getId());
                jsonObject.put(loadId, getIntent().getStringExtra("lid"));
                jsonObject.put(status, "3");
                jsonObject.put("comment_reject", "");

                getDecision(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.txt_accept) {
            clickOff = "accept";
            startActivity(new Intent(this, PaymentActivity.class).putExtra("tAmount", myLoadData.getLoadDetails().getOfferTotal()));
        } else if (id == R.id.txt_offer) {
            clickOff = "Offer";
            bidderOffers(this);
        } else if (id == R.id.txt_pay) {
            clickOff = "aceptandpay";
            startActivity(new Intent(this, PaymentActivity.class).putExtra("tAmount", myLoadData.getLoadDetails().getTotalAmt()));
        } else if (id == R.id.imgs) {

            startActivity(new Intent(this, BiderInfoActivity.class)
                    .putExtra("oid", myLoadData.getLoadDetails().getBidderId())
                    .putExtra("lid", myLoadData.getLoadDetails().getLorryIid()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (Utility.getInstance().paymentsucsses == 1) {
            Utility.getInstance().paymentsucsses = 0;
            if ("accept".equals(clickOff)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid", sessionmanager.getUserDetails().getId());
                    jsonObject.put(loadId, getIntent().getStringExtra("lid"));
                    jsonObject.put(status, "1");
                    jsonObject.put("comment_reject", "");
                    jsonObject.put("amount", myLoadData.getLoadDetails().getAmount());
                    jsonObject.put("amt_type", myLoadData.getLoadDetails().getAmtType());
                    jsonObject.put("total_amt", myLoadData.getLoadDetails().getTotalAmt());
                    jsonObject.put("wal_amt", Utility.getInstance().tWallet);
                    jsonObject.put("p_method_id", Utility.getInstance().paymentMID);
                    jsonObject.put("trans_id", Utility.getInstance().tragectionID);
                    jsonObject.put("description", "");

                    getDecision(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if ("aceptandpay".equals(clickOff)) {
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("uid", sessionmanager.getUserDetails().getId());
                    jsonObject1.put(loadId, getIntent().getStringExtra("lid"));
                    jsonObject1.put(status, "4");
                    jsonObject1.put("wal_amt", Utility.getInstance().tWallet);
                    jsonObject1.put("p_method_id", Utility.getInstance().paymentMID);
                    jsonObject1.put("trans_id", Utility.getInstance().tragectionID);
                    getDecision(jsonObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void sendReview(Context context) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.sendrview_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txtClick = rootView.findViewById(R.id.btn_load);
        ImageView img_p = rootView.findViewById(R.id.img_p);
        TextView txt_name = rootView.findViewById(R.id.txt_name);
        TextView txt_lorry = rootView.findViewById(R.id.txt_lorry);
        EditText edRevire = rootView.findViewById(R.id.ed_revire);
        RatingBar rating = rootView.findViewById(R.id.rating);
        txtClick.setText(getResources().getString(R.string.rate_to) + " " + myLoadData.getLoadDetails().getBidderName());

        Glide.with(this).load(APIClient.BASE_URL + "/" + myLoadData.getLoadDetails().getBidderImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(img_p);
        txt_name.setText("" + myLoadData.getLoadDetails().getBidderName());
        txt_lorry.setText("" + myLoadData.getLoadDetails().getLorryNumber());
        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            String ratin = String.valueOf(rating.getRating());
            getRateUpdate(ratin, edRevire.getText().toString());


        });
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();
    }

}