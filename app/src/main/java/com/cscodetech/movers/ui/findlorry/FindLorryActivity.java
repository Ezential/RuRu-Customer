package com.cscodetech.movers.ui.findlorry;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.AutoCompleteAdapter;
import com.cscodetech.movers.adepter.find.LorryFindAdapter;
import com.cscodetech.movers.adepter.find.VehicleFindAdapter;
import com.cscodetech.movers.model.TempLorryBook;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.model.find.Lorry;
import com.cscodetech.movers.model.find.LorrydataItem;
import com.cscodetech.movers.model.postload.CheckState;
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

public class FindLorryActivity extends BaseActivity implements VehicleFindAdapter.RecyclerTouchListener, LorryFindAdapter.RecyclerTouchListener, GetResult.MyListener {



    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.pickup)
    public AutoCompleteTextView pickup;
    @BindView(R.id.drop)
    public AutoCompleteTextView drop;
    @BindView(R.id.btn_next)
    public TextView btnNext;
    @BindView(R.id.txt_availeble)
    public TextView txtAvaileble;
    @BindView(R.id.recyclerView_vehicletype)
    public RecyclerView recyclerViewVehicletype;
    @BindView(R.id.recyclerView_lorry)
    public RecyclerView recyclerViewLorry;
    @BindView(R.id.lvl_empty)
    public LinearLayout lvlEmpty;

    CustPrograssbar custPrograssbar;
    UserLogin userLogin;

    AutoCompleteAdapter adapter;
    AutoCompleteAdapter adapterDrop;

    private PlacesClient placesClient;
    String pickStats;
    String  dropStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lorry);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();

        userLogin = sessionmanager.getUserDetails();
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                }, 1010);


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

        recyclerViewVehicletype.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVehicletype.setItemAnimator(new DefaultItemAnimator());


        recyclerViewLorry.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewLorry.setItemAnimator(new DefaultItemAnimator());


        findLorry("0","0");

    }

    private void findLorry(String pickid, String dropid) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());
            jsonObject.put("pick_state_id", pickid);
            jsonObject.put("drop_state_id", dropid);

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().findLorry(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getIsService(String pstate, String dstate) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pick_state_name", pstate);
            jsonObject.put("drop_state_name", dstate);

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().getstateid(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    Lorry lorry;
    int isBid = -1;
    LorryFindAdapter lorryFindAdapter;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                lorry = gson.fromJson(result.toString(), Lorry.class);

                if(lorry.getFindLorryData().size()!=0){
                    lvlEmpty.setVisibility(View.VISIBLE);
                    VehicleFindAdapter findAdapter  = new VehicleFindAdapter(this, lorry.getFindLorryData(), this);
                    recyclerViewVehicletype.setAdapter(findAdapter );


                    lorryFindAdapter = new LorryFindAdapter(this, lorry.getFindLorryData().get(0).getLorrydata(), this);
                    recyclerViewLorry.setAdapter(lorryFindAdapter);

                    txtAvaileble.setText(lorry.getFindLorryData().get(0).getLorrydata().size() + " Available");
                }else {
                    lvlEmpty.setVisibility(View.GONE);

                }



            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                CheckState checkState = gson.fromJson(result.toString(), CheckState.class);
                if (checkState.getResult().equalsIgnoreCase("true")) {
                    tempLorryBook.setPickStateId(checkState.getPickStateId());
                    tempLorryBook.setDropStateId(checkState.getDropStateId());
                    findLorry(checkState.getPickStateId(), checkState.getDropStateId());
                }
            }
        } catch (Exception e) {
            Log.e("Error","--"+e.getMessage());
        }

    }


    @OnClick({R.id.img_back,R.id.btn_next})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.btn_next) {
            getIsService(pickStats, dropStats);
        }
    }

    @Override
    public void onClickVehicleFindInfo(String item, int position) {
        txtAvaileble.setText(lorry.getFindLorryData().get(position).getLorrydata().size() + " Available");
        lorryFindAdapter = new LorryFindAdapter(this, lorry.getFindLorryData().get(position).getLorrydata(), this);
        recyclerViewLorry.setAdapter(lorryFindAdapter);

    }
    TempLorryBook tempLorryBook=new TempLorryBook();
    @Override
    public void onClickLorryFindInfo(LorrydataItem item, int position) {

        tempLorryBook.setUid(userLogin.getId());
        tempLorryBook.setVehicleId(item.getVehicleId());
        tempLorryBook.setLorryId(item.getLorryId());
        tempLorryBook.setLorryOwnerId(item.getLorryOwnerId());
        tempLorryBook.setTitle(item.getLorryTitle());
        tempLorryBook.setLorrywight(Integer.parseInt(item.getWeight()));

        startActivity(new Intent(this, SendLorryActivity.class).putExtra("lorrydata",tempLorryBook));
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
                            tempLorryBook.setPickLat(String.valueOf(task.getPlace().getLatLng().latitude));
                            tempLorryBook.setPickLng(String.valueOf(task.getPlace().getLatLng().longitude));
                            tempLorryBook.setPickupPoint(task.getPlace().getName() + "," + task.getPlace().getAddress());

                            for (int j = 0; j < task.getPlace().getAddressComponents().asList().size(); j++) {
                                if (task.getPlace().getAddressComponents().asList().get(j).getTypes().get(0).equalsIgnoreCase("administrative_area_level_1")) {
                                    pickStats = task.getPlace().getAddressComponents().asList().get(j).getName();
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
                            tempLorryBook.setDropLat(String.valueOf(task.getPlace().getLatLng().latitude));
                            tempLorryBook.setDropLng(String.valueOf(task.getPlace().getLatLng().longitude));
                            tempLorryBook.setDropPoint(task.getPlace().getName() + "," + task.getPlace().getAddress());

                            for (int j = 0; j < task.getPlace().getAddressComponents().asList().size(); j++) {
                                if (task.getPlace().getAddressComponents().asList().get(j).getTypes().get(0).equalsIgnoreCase("administrative_area_level_1")) {
                                    dropStats = task.getPlace().getAddressComponents().asList().get(j).getName();
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



}