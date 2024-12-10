package com.cscodetech.movers.ui.postload;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.BidsResponseAdapter;
import com.cscodetech.movers.model.RestResponse;
import com.cscodetech.movers.model.postload.BidStatus;
import com.cscodetech.movers.model.postload.LoadDetails;
import com.cscodetech.movers.model.postload.MyLoadData;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.BaseActivity;
import com.cscodetech.movers.ui.PaymentActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.SessionManager;
import com.cscodetech.movers.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PostLoadDetailsActivity extends BaseActivity implements BidsResponseAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.imgt)
    public ImageView imgt;
    @BindView(R.id.txt_title)
    public TextView txtTitle;
    @BindView(R.id.txt_price)
    public TextView txtPrice;
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
    @BindView(R.id.recyclerView_bidrs)
    public RecyclerView recyclerViewBidrs;
    @BindView(R.id.img_menu)
    public ImageView imgMenu;
    @BindView(R.id.lvl_nobids)
    public LinearLayout lvlNobids;
    @BindView(R.id.lvl_bid_response)
    public LinearLayout lvlBidResponse;
    @BindView(R.id.imgs)
    public ShapeableImageView imgs;
    @BindView(R.id.txt_name)
    public TextView txtName;
    @BindView(R.id.txt_offerprice)
    public TextView txtOfferprice;
    @BindView(R.id.txt_rates)
    public TextView txtRates;
    @BindView(R.id.lvl_bidderinfo)
    public LinearLayout lvlBidderinfo;
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
    @BindView(R.id.txt_secound)
    public TextView txtSecound;
    @BindView(R.id.txt_mits)
    public TextView txtMits;
    @BindView(R.id.txt_hours)
    public TextView txtHours;
    @BindView(R.id.txt_loadid)
    public TextView txtLoadid;
    @BindView(R.id.txt_order_stast)
    public TextView txtOrderStast;

    @BindView(R.id.txt_pricel)
    public TextView txtPricel;

    @BindView(R.id.txt_tonneorfixl)
    public TextView txtTonneorfixl;


    @BindView(R.id.txt_total)
    public TextView txtTotal;

    @BindView(R.id.lvl_tpayment_row)
    public LinearLayout lvlTpaymentRow;
    @BindView(R.id.lvl_paymentinfo)
    public LinearLayout lvlPaymentinfo;
    @BindView(R.id.lvl_timers)
    public LinearLayout lvlTimers;
    @BindView(R.id.btn_rating)
    public TextView btnRating;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load_details);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        recyclerViewBidrs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewBidrs.setItemAnimator(new DefaultItemAnimator());
        getLoadDetails();

    }

    @OnClick({R.id.img_back, R.id.img_menu, R.id.lvl_bidderinfo, R.id.btn_rating, R.id.imgs})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_rating:
                sendReview(this);

                break;
            case R.id.img_menu:
                PopupMenu popupMenu = new PopupMenu(this, imgMenu);
                popupMenu.inflate(R.menu.postload_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_edit) {
                            startActivity(new Intent(PostLoadDetailsActivity.this, PostLoadActivity.class).putExtra("edit", loadDetails));
                        } else if (itemId == R.id.navigation_delete) {
                            deletePostLoad(PostLoadDetailsActivity.this);

                            //do something
                        }
                        return false;
                    }
                });
                popupMenu.show();

                break;
            case R.id.lvl_bidderinfo:

                startActivity(new Intent(this, BiderInfoActivity.class));

                break;
            case R.id.imgs:

                startActivity(new Intent(this, BiderInfoActivity.class)
                        .putExtra("oid", loadDetails.getBidderId())
                        .putExtra("lid", loadDetails.getLorryIid()));
                break;
            default:
                break;

        }

    }

    BidStatus bidStatus;

    @Override
    public void onClickBidsInfo(BidStatus item, int position) {
        bidStatus = item;
        bidderDetails(this, item);
    }

    @Override
    public void onClickBidsInfoReject(BidStatus item, int position) {
        bidStatus = item;
        AlertDialog.Builder builder = new AlertDialog.Builder(PostLoadDetailsActivity.this);
        builder.setMessage(getResources().getString(R.string.doyoureject));
        builder.setTitle(getResources().getString(R.string.reject));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.continues), (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
            makeDecision("0");
        });
        builder.setNegativeButton(getResources().getString(R.string.no), (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                MyLoadData myLoadData = gson.fromJson(result.toString(), MyLoadData.class);
                loadDetails = myLoadData.getLoadDetails();

                txtTitle.setText("" + loadDetails.getVehicleTitle());
                txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + loadDetails.getAmount());
                txtTonneorfix.setText(" /" + loadDetails.getAmtType());
                txtPick.setText("" + loadDetails.getPickupState());
                txtPickaddress.setText("" + loadDetails.getPickupPoint());
                txtDrop.setText("" + loadDetails.getDropState());
                txtDropaddress.setText("" + loadDetails.getDropPoint());
                txtDate.setText("" + Utility.getInstance().parseDateToddMMyy(loadDetails.getPostDate()));
                txtTonne.setText("" + loadDetails.getWeight());
                txtMaterial.setText("" + loadDetails.getMaterialName());
                txtLoadid.setText(getResources().getString(R.string.load) + " #" + loadDetails.getId());
                Glide.with(this).load(APIClient.BASE_URL + "/" + loadDetails.getVehicleImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgt);


                if (myLoadData.getLoadDetails().getAmtType().equalsIgnoreCase("Tonne")) {
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getAmount());
                    txtTonneorfix.setText("/ " + myLoadData.getLoadDetails().getAmtType());
                    txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + myLoadData.getLoadDetails().getTotalAmt());
                } else {
                    txtTotal.setVisibility(View.GONE);
                }


                if (loadDetails.getHoursType().equalsIgnoreCase("infinite")) {
                    lvlTimers.setVisibility(View.GONE);
                } else {

                    long milliseconds = loadDetails.getSvisibleHours() * 60 * 60 * 1000;
                    new CountDownTimer(milliseconds, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (millisUntilFinished / 3600000) % 24;
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            txtHours.setText(f.format(hour));
                            txtMits.setText(f.format(min));
                            txtSecound.setText(f.format(sec));
                        }

                        // When the task is over it will print 00:00:00 there
                        public void onFinish() {
                            txtHours.setText("00:00:00");
                        }
                    }.start();


                }

                if (myLoadData.getLoadDetails().getBidStatus().size() == 0 && myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("0")) {
                    loadDetails = myLoadData.getLoadDetails();
                    lvlBidResponse.setVisibility(View.GONE);
                    lvlBidderinfo.setVisibility(View.GONE);
                    lvlPaymentinfo.setVisibility(View.GONE);
                    imgMenu.setVisibility(View.VISIBLE);
                } else if (myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("0")) {
                    lvlBidResponse.setVisibility(View.VISIBLE);
                    lvlNobids.setVisibility(View.GONE);
                    lvlBidderinfo.setVisibility(View.GONE);
                    lvlPaymentinfo.setVisibility(View.GONE);

                    BidsResponseAdapter bidsResponseAdapter = new BidsResponseAdapter(this, myLoadData.getLoadDetails().getBidStatus(), this);
                    recyclerViewBidrs.setAdapter(bidsResponseAdapter);
                } else {
                    lvlBidResponse.setVisibility(View.GONE);
                    if (myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("3")) {
                        lvlNobids.setVisibility(View.GONE);
                    } else {
                        lvlNobids.setVisibility(View.VISIBLE);
                        txtOrderStast.setText(myLoadData.getLoadDetails().getMessage());
                    }

                    lvlBidderinfo.setVisibility(View.VISIBLE);
                    lvlPaymentinfo.setVisibility(View.VISIBLE);

                    Glide.with(this).load(APIClient.BASE_URL + "/" + loadDetails.getBidderImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgs);

                    txtName.setText("" + loadDetails.getBidderName());
                    txtPricel.setText(sessionManager.getStringData(SessionManager.currency) + loadDetails.getAmount());
                    txtRates.setText(loadDetails.getRate());
                    txtTonneorfixl.setText(" /" + loadDetails.getAmtType());
                    txtOfferprice.setText(sessionManager.getStringData(SessionManager.currency) + loadDetails.getTotalAmt());


                    txtPmethod.setText("" + loadDetails.getpMethodName());
                    txtTrasaction.setText("" + loadDetails.getOrderTransactionId());
                    txtSubtotal.setText(sessionManager.getStringData(SessionManager.currency) + loadDetails.getAmount());
                    txtTpayment.setText(sessionManager.getStringData(SessionManager.currency) + loadDetails.getTotalAmt());

                    if (loadDetails.getWalAmt().equalsIgnoreCase("0")) {
                        lvlWalletRow.setVisibility(View.GONE);
                    } else {
                        lvlWalletRow.setVisibility(View.VISIBLE);
                        txtWallet.setText(getResources().getString(R.string.load) + " #" + loadDetails.getWalAmt());
                    }
                    if (myLoadData.getLoadDetails().getFlowId().equalsIgnoreCase("3") && myLoadData.getLoadDetails().getIsRate().equalsIgnoreCase("0")) {
                        btnRating.setVisibility(View.VISIBLE);
                        btnRating.setText(getResources().getString(R.string.rate_to) + " " + loadDetails.getBidderName());

                    }

                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    Utility.getInstance().removepost = true;
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("4")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this,response.getResponseMsg(),Toast.LENGTH_SHORT).show();
                if (response.getResult().equals("true")) {

                    finish();
                }
            }
        } catch (Exception e) {
            Log.e("Errror--", "-" + e.toString());
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
        txtClick.setText(getResources().getString(R.string.rate_to) + " " + loadDetails.getBidderName());

        Glide.with(this).load(APIClient.BASE_URL + "/" + loadDetails.getBidderImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(img_p);
        txt_name.setText("" + loadDetails.getBidderName());
        txt_lorry.setText("" + loadDetails.getLorryNumber());
        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            String ratin = String.valueOf(rating.getRating());
            getRateUpdate(ratin, edRevire.getText().toString());


        });
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();
    }


    private void getRateUpdate(String rate, String ratetext) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails().getId());
            jsonObject.put("load_id", getIntent().getStringExtra("lid"));
            jsonObject.put("total_trate", rate);
            jsonObject.put("rate_ttext", ratetext);
            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().rateUpdate(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");
            custPrograssbar.prograssCreate(PostLoadDetailsActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getLoadDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails().getId());
            jsonObject.put("load_id", getIntent().getStringExtra("lid"));

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().loadDetails(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deletePostLoad(Context context) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.delete_loadpos_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txtClick = rootView.findViewById(R.id.txt_reject);
        TextView btnYes = rootView.findViewById(R.id.btn_yes);
        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();


        });

        btnYes.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            deleteLoadDetails();

        });
        mBottomSheetDialog.show();
    }


    private void deleteLoadDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails().getId());
            jsonObject.put("record_id", getIntent().getStringExtra("lid"));

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().deleteLoad(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");

            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void makeDecision(String status) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails().getId());
            jsonObject.put("status", status);
            jsonObject.put("load_id", loadDetails.getId());
            jsonObject.put("owner_id", bidStatus.getBidderId());
            jsonObject.put("lorry_id", bidStatus.getLorryid());
            jsonObject.put("amount", bidStatus.getAmount());
            jsonObject.put("amt_type", bidStatus.getAmtType());
            jsonObject.put("total_amt", bidStatus.getTotalAmt());
            jsonObject.put("wal_amt", Utility.getInstance().tWallet);
            jsonObject.put("p_method_id", Utility.getInstance().paymentMID);
            jsonObject.put("trans_id", Utility.getInstance().tragectionID);
            jsonObject.put("description", bidStatus.getDescription());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().makeDecision(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");

            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void bidderDetails(Context context, BidStatus item) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.biddetails_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txt_name = rootView.findViewById(R.id.txt_name);
        TextView txt_price = rootView.findViewById(R.id.txt_price);
        TextView txt_rates = rootView.findViewById(R.id.txt_rates);
        TextView txt_nooflorry = rootView.findViewById(R.id.txt_nooflorry);
        TextView txt_since = rootView.findViewById(R.id.txt_since);
        TextView txt_desc = rootView.findViewById(R.id.txt_desc);
        TextView txtClick = rootView.findViewById(R.id.btn_load);
        ImageView imgs = rootView.findViewById(R.id.imgs);

        txt_name.setText("" + item.getBidderName());
        txt_price.setText(sessionManager.getStringData(SessionManager.currency) + item.getTotalAmt());
        txt_rates.setText("" + item.getRate());
        txt_nooflorry.setText("" + item.getTotalLorry() + " " + getResources().getString(R.string.lorry));
        txt_since.setText(getResources().getString(R.string.since) + " " + item.getJoinDate());
        txt_desc.setText("" + item.getDescription());
        Log.e("imageuri","-->"+item.getBidderImg());
        Glide.with(this).load(APIClient.BASE_URL + "/" + item.getBidderImg()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgs);


        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();

            startActivity(new Intent(this, PaymentActivity.class).putExtra("tAmount", item.getTotalAmt()));

        });
        mBottomSheetDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.getInstance().paymentsucsses == 1) {
            Utility.getInstance().paymentsucsses = 0;
            makeDecision("1");
        }
    }
}