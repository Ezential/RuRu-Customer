package com.ruru.customer.ui;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruru.customer.R;
import com.ruru.customer.model.Contry;
import com.ruru.customer.model.RestResponse;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.retrofit.GetResult;
import com.ruru.customer.utils.CustPrograssbar;
import com.ruru.customer.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
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

public class SignUpActivity extends BaseActivity implements GetResult.MyListener {


    @BindView(R.id.l1)
    public LinearLayout l1;
    @BindView(R.id.spinnerautocompet)
    public AutoCompleteTextView spinnerautocompet;
    @BindView(R.id.ed_username)
    public TextInputEditText edUsername;
    @BindView(R.id.edit_fullname)
    public TextInputEditText editFullname;
    @BindView(R.id.edit_email)
    public TextInputEditText editEmail;
    @BindView(R.id.edit_password)
    public TextInputEditText editPassword;
    @BindView(R.id.edit_refercode)
    public TextInputEditText editRefercode;
    @BindView(R.id.chackbox)
    public CheckBox chackbox;
    @BindView(R.id.btn_signup)
    public TextView btnSignup;
    @BindView(R.id.lvl_login)
    public LinearLayout lvlLogin;
    CustPrograssbar custPrograssbar;
    String applicationjson="application/json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        custPrograssbar=new CustPrograssbar();
        getCode();

    }
    private void checkMobile() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edUsername.getText().toString());
            jsonObject.put("ccode", spinnerautocompet.getText().toString());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse(applicationjson));
            Call<JsonObject> call = APIClient.getInterface().mobileCheck(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(SignUpActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_signup, R.id.lvl_login})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_signup) {
            if (validationCreate()) {
                checkMobile();

            }
        } else if (id == R.id.lvl_login) {
            finish();
        }
    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse(applicationjson));
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult(this);
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {


            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < contry.getCountryCode().size(); i++) {
                    if (contry.getCountryCode().get(i).getStatus().equalsIgnoreCase("1")) {
                        arealist.add(contry.getCountryCode().get(i).getCcode());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, arealist);
                spinnerautocompet.setText(arealist.get(0));
                spinnerautocompet.setAdapter(adapter);
            }else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {

                    Utility.getInstance().isvarification = 1;
                    UserLogin userLogin = new UserLogin();
                    userLogin.setCcode(spinnerautocompet.getText().toString());
                    userLogin.setMobile(edUsername.getText().toString());
                    userLogin.setName(editFullname.getText().toString());
                    userLogin.setEmail(editEmail.getText().toString());
                    userLogin.setPassword(editPassword.getText().toString());
                    userLogin.setRefercode(editRefercode.getText().toString());
                    sessionmanager.setUserDetails(userLogin);
                    startActivity(new Intent(this, VerificationActivity.class));

                } else {
                    Toast.makeText(this, "Number is register", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Log.e("Error","-"+e.getMessage());

        }
    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(edUsername.getText().toString())) {
            edUsername.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(editFullname.getText().toString())) {
            editFullname.setError("Enter Name");
            return false;
        }

        if (TextUtils.isEmpty(editEmail.getText().toString())) {
            editEmail.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText().toString())) {
            editPassword.setError("Enter Password");
            return false;
        }

        return true;
    }
}