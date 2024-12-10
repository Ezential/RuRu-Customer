package com.cscodetech.movers.ui.postload;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.AutoCompleteAdapter;
import com.cscodetech.movers.model.TempPostLoad;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.model.postload.CheckState;
import com.cscodetech.movers.model.postload.LoadDetails;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.BaseActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PostLoadActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
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
    @BindView(R.id.btn_next)
    public TextView btnNext;
    @BindView(R.id.txt_actionbar)
    public TextView txtActionbar;

    AutoCompleteAdapter adapter;
    AutoCompleteAdapter adapterDrop;

    private PlacesClient placesClient;

    TempPostLoad tempPostLoad;
    UserLogin userLogin;
    CustPrograssbar custPrograssbar;
    LoadDetails loadDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load);
        ButterKnife.bind(this);

        userLogin = sessionmanager.getUserDetails();
        custPrograssbar = new CustPrograssbar();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);

        pickup.setThreshold(1);
        pickup.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        pickup.setAdapter(adapter);

        drop.setThreshold(1);
        drop.setOnItemClickListener(autocompleteClickListenerDrop);
        adapterDrop = new AutoCompleteAdapter(this, placesClient);
        drop.setAdapter(adapterDrop);
        tempPostLoad = new TempPostLoad();
        tempPostLoad.setUid(userLogin.getId());
        loadDetails = getIntent().getParcelableExtra("edit");
        if (loadDetails != null) {
            txtActionbar.setText(getResources().getString(R.string.edit) + " " + getResources().getString(R.string.post_load) + " #" + loadDetails.getId());
            pickup.setText(loadDetails.getPickupPoint());
            drop.setText(loadDetails.getDropPoint());
            edMaterialname.setText(loadDetails.getMaterialName());
            edTypeweight.setText(loadDetails.getWeight());
            edDescription.setText(loadDetails.getDescription());

            tempPostLoad.setUid(userLogin.getId());
            tempPostLoad.setAmtType(loadDetails.getAmtType());
            tempPostLoad.setTotalAmt(loadDetails.getTotalAmt());
            tempPostLoad.setAmount(loadDetails.getAmount());
            tempPostLoad.setVisibleHours(loadDetails.getVisibleHours());

            tempPostLoad.setPickupPoint(loadDetails.getPickupPoint());
            tempPostLoad.setPickLat(loadDetails.getPickLat());
            tempPostLoad.setPickLng(loadDetails.getPickLng());
            tempPostLoad.setDropPoint(loadDetails.getDropPoint());
            tempPostLoad.setDropLat(loadDetails.getDropLat());
            tempPostLoad.setDropLng(loadDetails.getDropLng());
            tempPostLoad.setWeight(loadDetails.getWeight());
            tempPostLoad.setMaterialName(loadDetails.getMaterialName());

            tempPostLoad.setPickState(loadDetails.getPickupState());
            tempPostLoad.setDropState(loadDetails.getDropState());
            tempPostLoad.setHoursType(loadDetails.getHoursType());
        }


    }

    @OnClick({R.id.img_back, R.id.btn_next})

    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.btn_next && validationCreate()) {
            getIsservice();
        }
    }

    private final AdapterView.OnItemClickListener autocompleteClickListener = (adapterView, view, i, l) -> {
        try {
            final AutocompletePrediction item = adapter.getItem(i);
            String placeID = null;
            if (item != null) {
                placeID = item.getPlaceId();
            }

            List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS);

            FetchPlaceRequest request = placeID != null
                    ? FetchPlaceRequest.builder(placeID, placeFields).build()
                    : null;

            if (request != null) {
                placesClient.fetchPlace(request)
                        .addOnSuccessListener(task -> {
                            tempPostLoad.setPickLat(String.valueOf(task.getPlace().getLatLng().latitude));
                            tempPostLoad.setPickLng(String.valueOf(task.getPlace().getLatLng().longitude));
                            tempPostLoad.setPickupPoint(task.getPlace().getName() + "," + task.getPlace().getAddress());
                            Log.e("task.etName()", "" + task.getPlace().getAddressComponents());

                            for (int j = 0; j < task.getPlace().getAddressComponents().asList().size(); j++) {
                                if (task.getPlace().getAddressComponents().asList().get(j).getTypes().get(0).equalsIgnoreCase("administrative_area_level_1")) {
                                    Log.e("33333333333333333333333", "" + task.getPlace().getAddressComponents().asList().get(j).getName());
                                    tempPostLoad.setPickState(task.getPlace().getAddressComponents().asList().get(j).getName());
                                    break;
                                }
                            }
                        })
                        .addOnFailureListener(e -> e.printStackTrace());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };


    private final AdapterView.OnItemClickListener autocompleteClickListenerDrop = (adapterView, view, i, l) -> {
        try {
            final AutocompletePrediction item = adapterDrop.getItem(i);
            String placeID = null;
            if (item != null) {
                placeID = item.getPlaceId();
            }

            List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS);

            FetchPlaceRequest request = placeID != null
                    ? FetchPlaceRequest.builder(placeID, placeFields).build()
                    : null;

            if (request != null) {
                placesClient.fetchPlace(request)
                        .addOnSuccessListener(task -> {
                            tempPostLoad.setDropLat(String.valueOf(task.getPlace().getLatLng().latitude));
                            tempPostLoad.setDropLng(String.valueOf(task.getPlace().getLatLng().longitude));
                            tempPostLoad.setDropPoint(task.getPlace().getName() + "," + task.getPlace().getAddress());

                            for (int j = 0; j < task.getPlace().getAddressComponents().asList().size(); j++) {
                                if (task.getPlace().getAddressComponents().asList().get(j).getTypes().get(0).equalsIgnoreCase("administrative_area_level_1")) {
                                    Log.e("33333333333333333333333", "" + task.getPlace().getAddressComponents().asList().get(j).getName());
                                    tempPostLoad.setDropState(task.getPlace().getAddressComponents().asList().get(j).getName());
                                    break;
                                }
                            }
                        })
                        .addOnFailureListener(e -> e.printStackTrace());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };


    private void getIsservice() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pick_state_name", tempPostLoad.getPickState());
            jsonObject.put("drop_state_name", tempPostLoad.getDropState());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().getstateid(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
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
                CheckState checkState = gson.fromJson(result.toString(), CheckState.class);
                if (!checkState.getPickStateId().equalsIgnoreCase("0") && !checkState.getDropStateId().equalsIgnoreCase("0")) {
                    tempPostLoad.setMaterialName(edMaterialname.getText().toString());
                    tempPostLoad.setWeight(edTypeweight.getText().toString());
                    tempPostLoad.setDescription(edDescription.getText().toString());
                    tempPostLoad.setPickStateId(checkState.getPickStateId());
                    tempPostLoad.setDropStateId(checkState.getDropStateId());
                    startActivity(new Intent(this, PostLoadVevikalActivity.class).putExtra("temppost", tempPostLoad).putExtra("edit", loadDetails));
                } else {

                    if (checkState.getPickStateId().equalsIgnoreCase("0")) {
                        tempPostLoad.setPickLng("");
                        tempPostLoad.setPickLat("");
                        tempPostLoad.setPickupPoint("");
                        tempPostLoad.setPickStateId("");
                        pickup.setText("");
                    }
                    if (checkState.getDropStateId().equalsIgnoreCase("0")) {
                        tempPostLoad.setDropLat("");
                        tempPostLoad.setDropLng("");
                        tempPostLoad.setDropPoint("");
                        tempPostLoad.setDropStateId("");
                        drop.setText("");

                    }
                    Toast.makeText(this, checkState.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-" + e.getMessage());

        }

    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(pickup.getText().toString())) {
            pickup.setError("");
            return false;
        }
        if (TextUtils.isEmpty(drop.getText().toString())) {
            drop.setError("");
            return false;
        }

        if (TextUtils.isEmpty(edMaterialname.getText().toString())) {
            edMaterialname.setError("");
            return false;
        }
        if (TextUtils.isEmpty(edTypeweight.getText().toString())) {
            edTypeweight.setError("");
            return false;
        }


        return true;
    }
}