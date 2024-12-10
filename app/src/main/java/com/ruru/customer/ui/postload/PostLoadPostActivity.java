package com.ruru.customer.ui.postload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruru.customer.R;
import com.ruru.customer.model.RestResponse;
import com.ruru.customer.model.TempPostLoad;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.model.postload.LoadDetails;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.retrofit.GetResult;
import com.ruru.customer.ui.BaseActivity;
import com.ruru.customer.ui.HomeActivity;
import com.ruru.customer.utils.CustPrograssbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class PostLoadPostActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.ed_amount)
    public EditText edAmount;
    @BindView(R.id.swichtonne)
    public Switch swichtonne;
    @BindView(R.id.ed_number)
    public EditText edNumber;
    @BindView(R.id.swich_notfix)
    public Switch swichNotfix;
    @BindView(R.id.btn_postloaded)
    public TextView btnPostloaded;
    @BindView(R.id.txt_actionbar)
    public TextView txtActionbar;

    @BindView(R.id.pickup_contect)
    public TextView pickupContect;

    @BindView(R.id.drop_contect)
    public TextView dropDontect;
    TempPostLoad tempPostLoad;
    CustPrograssbar custPrograssbar;
    LoadDetails loadDetails;
    String fixed = "Fixed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load_post);
        ButterKnife.bind(this);
        tempPostLoad = (TempPostLoad) getIntent().getSerializableExtra("temppost");
        custPrograssbar = new CustPrograssbar();
        loadDetails = getIntent().getParcelableExtra("edit");

        if (loadDetails != null) {
            txtActionbar.setText(getResources().getString(R.string.edit) + " " + getResources().getString(R.string.post_load) + " #" + loadDetails.getId());
            edAmount.setText("" + loadDetails.getAmount());
            edNumber.setText("" + loadDetails.getVisibleHours());
            pickupContect.setText(""+loadDetails.getPickMobile()+" ("+loadDetails.getPickName()+")");
            dropDontect.setText(""+loadDetails.getDropMobile()+" ("+loadDetails.getDropName()+")");
            if (loadDetails.getHoursType().equalsIgnoreCase("infinite")) {
                swichNotfix.setChecked(true);
                swichNotfix.setText(getResources().getString(R.string.notfix));
                edNumber.setText(loadDetails.getVisibleHours());
            }

            if (!loadDetails.getAmtType().equalsIgnoreCase(fixed)) {
                swichtonne.setChecked(true);
                swichtonne.setText(getResources().getString(R.string.pertonnes));
                edAmount.setText(loadDetails.getAmount());
            }
        }

        swichtonne.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                if (b) {
                    swichtonne.setText(getResources().getString(R.string.pertonnes));
                    tempPostLoad.setAmtType("Tonne");
                    double tamout = Double.parseDouble(edAmount.getText().toString()) * Double.parseDouble(tempPostLoad.getWeight());
                    tempPostLoad.setTotalAmt("" + tamout);


                } else {
                    swichtonne.setText(getResources().getString(R.string.fix));
                    tempPostLoad.setAmtType(fixed);
                    tempPostLoad.setTotalAmt("" + edAmount.getText().toString());


                }
            }

        });

        swichNotfix.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                swichNotfix.setText(getResources().getString(R.string.notfix));
                edNumber.setText("0");
                edNumber.setEnabled(false);
            } else {
                edNumber.setEnabled(true);
                swichNotfix.setText(getResources().getString(R.string.hours));
            }
        });


    }

    public void postLoaded(Context context) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.loadposted_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txtClick = rootView.findViewById(R.id.btn_load);
        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();

    }


    @OnClick({R.id.img_back, R.id.btn_postloaded, R.id.pickup_contect, R.id.drop_contect})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.btn_postloaded && validationCreate()) {
            tempPostLoad.setAmount(edAmount.getText().toString());
            tempPostLoad.setVisibleHours(edNumber.getText().toString());
            postLoad();
        } else if (id == R.id.pickup_contect) {
            bottonConfirm(pickupContect, "pick");
        } else if (id == R.id.drop_contect) {
            bottonConfirm(dropDontect, "drop");
        }
    }

    public boolean validationCreate() {
        if (!swichNotfix.isChecked()) {
            edNumber.setEnabled(true);
            swichNotfix.setText(getResources().getString(R.string.hours));
        }

        if (!swichtonne.isChecked()) {

            swichtonne.setText(getResources().getString(R.string.fix));
            tempPostLoad.setAmtType(fixed);
            tempPostLoad.setTotalAmt("" + edAmount.getText().toString());
        }
        if (TextUtils.isEmpty(edAmount.getText().toString())) {
            edAmount.setError("");
            return false;
        }
        if (TextUtils.isEmpty(edNumber.getText().toString())) {
            edNumber.setError("");
            return false;
        }
        if (tempPostLoad.getPickName() == null && tempPostLoad.getPickMobile() == null) {
            pickupContect.setError("");
            return false;
        }
        if (tempPostLoad.getDropName() == null && tempPostLoad.getDropMobile() == null) {
            dropDontect.setError("");

            return false;
        }

        return true;
    }

    private void postLoad() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", tempPostLoad.getUid());
            jsonObject.put("pickup_point", tempPostLoad.getPickupPoint());
            jsonObject.put("drop_point", tempPostLoad.getDropPoint());
            jsonObject.put("material_name", tempPostLoad.getMaterialName());
            jsonObject.put("weight", tempPostLoad.getWeight());
            jsonObject.put("description", tempPostLoad.getDescription());
            jsonObject.put("vehicle_id", tempPostLoad.getVehicleId());
            jsonObject.put("amount", tempPostLoad.getAmount());
            jsonObject.put("amt_type", tempPostLoad.getAmtType());
            jsonObject.put("visible_hours", tempPostLoad.getVisibleHours());
            jsonObject.put("total_amt", tempPostLoad.getTotalAmt());
            jsonObject.put("pick_lat", tempPostLoad.getPickLat());
            jsonObject.put("pick_lng", tempPostLoad.getPickLng());
            jsonObject.put("drop_lat", tempPostLoad.getDropLat());
            jsonObject.put("drop_lng", tempPostLoad.getDropLng());
            jsonObject.put("pick_state_id", tempPostLoad.getPickStateId());
            jsonObject.put("drop_state_id", tempPostLoad.getDropStateId());

            jsonObject.put("pick_name", tempPostLoad.getPickName());
            jsonObject.put("pick_mobile", tempPostLoad.getPickMobile());
            jsonObject.put("drop_name", tempPostLoad.getDropName());
            jsonObject.put("drop_mobile", tempPostLoad.getDropMobile());

            if (loadDetails != null) {
                jsonObject.put("record_id", loadDetails.getId());
                RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
                Call<JsonObject> call = APIClient.getInterface().editLoad(bodyRequest);
                GetResult getResult = new GetResult(this);
                getResult.setMyListener(this);
                getResult.callForLogin(call, "1");
            } else {
                RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
                Call<JsonObject> call = APIClient.getInterface().postLoad(bodyRequest);
                GetResult getResult = new GetResult(this);
                getResult.setMyListener(this);
                getResult.callForLogin(call, "1");
            }


            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    postLoaded(this);

                } else {
                    Toast.makeText(PostLoadPostActivity.this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "--> " + e.getMessage());

        }
    }

    public void bottonConfirm(TextView contact, String type) {
        UserLogin userLogin = sessionmanager.getUserDetails();
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(PostLoadPostActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.customeconfirmdetails, null);
        mBottomSheetDialog.setContentView(sheetView);
        CheckBox chUser = sheetView.findViewById(R.id.ch_user);
        EditText edName = sheetView.findViewById(R.id.ed_name);
        EditText edmobile = sheetView.findViewById(R.id.ed_mobile);
        TextView txtChoose = sheetView.findViewById(R.id.btn_send);

        chUser.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                edName.setText(userLogin.getName());
                edmobile.setText(userLogin.getMobile());
            } else {
                edmobile.setText("");
                edName.setText("");
            }
        });
        txtChoose.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(edmobile.getText().toString()) && !TextUtils.isEmpty(edName.getText().toString())) {
                mBottomSheetDialog.cancel();
                if (type.equalsIgnoreCase("pick")) {
                    tempPostLoad.setPickName("" + edName.getText().toString());
                    tempPostLoad.setPickMobile("" + edmobile.getText().toString());
                } else {
                    tempPostLoad.setDropName("" + edName.getText().toString());
                    tempPostLoad.setDropMobile("" + edmobile.getText().toString());
                }
                contact.setText("" + edmobile.getText().toString() + " (" + edName.getText().toString()+")");

            }
        });
        mBottomSheetDialog.show();
    }
}