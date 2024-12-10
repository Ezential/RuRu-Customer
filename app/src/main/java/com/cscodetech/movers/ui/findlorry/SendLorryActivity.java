package com.cscodetech.movers.ui.findlorry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.movers.R;
import com.cscodetech.movers.model.RestResponse;
import com.cscodetech.movers.model.TempLorryBook;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.BaseActivity;
import com.cscodetech.movers.ui.HomeActivity;
import com.cscodetech.movers.ui.postload.PostLoadPostActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
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

public class SendLorryActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_title)
    public TextView txtTitle;
    @BindView(R.id.pickup)
    public AutoCompleteTextView pickup;
    @BindView(R.id.drop)
    public AutoCompleteTextView drop;
    @BindView(R.id.ed_materialname)
    public EditText edMaterialname;
    @BindView(R.id.ed_typeweight)
    public EditText edTypeweight;
    @BindView(R.id.ed_description)
    public EditText edDescription;
    @BindView(R.id.ed_amount)
    public EditText edAmount;
    @BindView(R.id.swichtonne)
    public Switch swichtonne;
    @BindView(R.id.btnsendrequest)
    public TextView btnsendrequest;
    TempLorryBook tempLorryBook;
    CustPrograssbar custPrograssbar;
    @BindView(R.id.pickup_contect)
    public TextView pickupContect;

    @BindView(R.id.drop_contect)
    public TextView dropDontect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_lorry);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        tempLorryBook = (TempLorryBook) getIntent().getSerializableExtra("lorrydata");

        txtTitle.setText("" + tempLorryBook.getTitle());
        pickup.setText("" + tempLorryBook.getPickupPoint());
        drop.setText("" + tempLorryBook.getDropPoint());

        swichtonne.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!TextUtils.isEmpty(edAmount.getText().toString()) && !TextUtils.isEmpty(edTypeweight.getText().toString())) {
                if (b) {
                    swichtonne.setText("Per Tonnes");
                    tempLorryBook.setAmtType("Tonne");
                    double tamout = Double.parseDouble(edAmount.getText().toString()) * Double.parseDouble(edTypeweight.getText().toString());
                    tempLorryBook.setTotalAmt("" + tamout);


                } else {
                    swichtonne.setText("Fix");
                    tempLorryBook.setAmtType("Fixed");
                    tempLorryBook.setTotalAmt("" + edAmount.getText().toString());
                }
            }

        });


    }

    private void getIsService() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", tempLorryBook.getUid());
            jsonObject.put("pickup_point", tempLorryBook.getPickupPoint());
            jsonObject.put("drop_point", tempLorryBook.getDropPoint());
            jsonObject.put("material_name", edMaterialname.getText().toString());
            jsonObject.put("weight", edTypeweight.getText().toString());
            jsonObject.put("description", edDescription.getText().toString());
            jsonObject.put("vehicle_id", tempLorryBook.getVehicleId());
            jsonObject.put("amount", edAmount.getText().toString());
            jsonObject.put("amt_type", tempLorryBook.getAmtType());
            jsonObject.put("visible_hours", "0");
            jsonObject.put("total_amt", tempLorryBook.getTotalAmt());
            jsonObject.put("pick_lat", tempLorryBook.getPickLat());
            jsonObject.put("pick_lng", tempLorryBook.getPickLng());
            jsonObject.put("drop_lat", tempLorryBook.getDropLat());
            jsonObject.put("drop_lng", tempLorryBook.getDropLng());
            jsonObject.put("pick_state_id", tempLorryBook.getPickStateId());
            jsonObject.put("lorry_id", tempLorryBook.getLorryId());
            jsonObject.put("lorry_owner_id", tempLorryBook.getLorryOwnerId());
            jsonObject.put("drop_state_id", tempLorryBook.getDropStateId());

            jsonObject.put("pick_name", tempLorryBook.getPickname());
            jsonObject.put("pick_mobile", tempLorryBook.getPickmobile());
            jsonObject.put("drop_name", tempLorryBook.getDeopname());
            jsonObject.put("drop_mobile", tempLorryBook.getDropmobile());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().bookLorry(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    sendRequest(this);
                } else {
                    Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-" + e.getMessage());

        }
    }

    public void sendRequest(Context context) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.sendrequest_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        TextView txtClick = rootView.findViewById(R.id.btn_load);
        txtClick.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        });
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();
    }


    @OnClick({R.id.img_back, R.id.btnsendrequest, R.id.pickup_contect, R.id.drop_contect})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.btnsendrequest && validationCreate()) {
            getIsService();
        } else if (id == R.id.pickup_contect) {
            bottonConfirm(pickupContect, "pick");
        } else if (id == R.id.drop_contect) {
            bottonConfirm(dropDontect, "drop");
        }
    }

    public boolean validationCreate() {
        if (!swichtonne.isChecked()) {
            swichtonne.setText("Fix");
            tempLorryBook.setAmtType("Fixed");
            tempLorryBook.setTotalAmt("" + edAmount.getText().toString());
        }

        if (TextUtils.isEmpty(edMaterialname.getText().toString())) {
            edMaterialname.setError("");
            return false;
        }
        if (TextUtils.isEmpty(edTypeweight.getText().toString())) {
            edTypeweight.setError("");
            return false;
        }

        if (TextUtils.isEmpty(edAmount.getText().toString())) {
            edAmount.setError("");
            return false;
        }

        if (tempLorryBook.getPickname() == null && tempLorryBook.getPickmobile() == null) {
            pickupContect.setError("");
            return false;
        }
        if (tempLorryBook.getDeopname() == null && tempLorryBook.getDropmobile() == null) {
            dropDontect.setError("");

            return false;
        }

        if (edTypeweight.getText().toString() != null && Integer.parseInt(edTypeweight.getText().toString()) <= tempLorryBook.getLorrywight()) {
            return true;

        } else {
            edTypeweight.setText("");
            Toast.makeText(SendLorryActivity.this, "The maximum number of tonnes you can load is limited to the capacity of a lorry.", Toast.LENGTH_LONG).show();
            return false;
        }




    }

    public void bottonConfirm(TextView contact, String type) {
        UserLogin userLogin = sessionmanager.getUserDetails();
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(SendLorryActivity.this);
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
                    tempLorryBook.setPickname("" + edName.getText().toString());
                    tempLorryBook.setPickmobile("" + edmobile.getText().toString());
                } else {
                    tempLorryBook.setDeopname("" + edName.getText().toString());
                    tempLorryBook.setDropmobile("" + edmobile.getText().toString());
                }
                contact.setText("" + edmobile.getText().toString() + " (" + edName.getText().toString() + ")");

            }
        });
        mBottomSheetDialog.show();
    }

}