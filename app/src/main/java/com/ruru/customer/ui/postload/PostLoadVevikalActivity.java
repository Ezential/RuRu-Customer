package com.ruru.customer.ui.postload;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruru.customer.R;
import com.ruru.customer.adepter.VehicleAdapter;
import com.ruru.customer.model.TempPostLoad;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.model.postload.LoadDetails;
import com.ruru.customer.model.postload.Vehicle;
import com.ruru.customer.model.postload.VehicleDataItem;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.retrofit.GetResult;
import com.ruru.customer.ui.BaseActivity;
import com.ruru.customer.utils.CustPrograssbar;
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
public class PostLoadVevikalActivity extends BaseActivity implements VehicleAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.recyclerview_vehicle)
    public RecyclerView recyclerviewVehicle;
    @BindView(R.id.btn_next)
    public TextView btnNext;
    @BindView(R.id.txt_actionbar)
    public TextView txtActionbar;

    UserLogin userLogin;
    CustPrograssbar custPrograssbar;
    TempPostLoad tempPostLoad;
    LoadDetails loadDetails;
    String applicationjson="application/json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load_vevikal);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        userLogin = sessionmanager.getUserDetails();
        tempPostLoad = (TempPostLoad) getIntent().getSerializableExtra("temppost");

        recyclerviewVehicle.setLayoutManager(new GridLayoutManager(this, 2, VERTICAL, false));
        recyclerviewVehicle.setItemAnimator(new DefaultItemAnimator());
        loadDetails=getIntent().getParcelableExtra("edit");
        if(loadDetails!=null){
            txtActionbar.setText(getResources().getString(R.string.edit)+" "+getResources().getString(R.string.post_load)+" #"+loadDetails.getId());
        }
        getVehicle();


    }

    private void getVehicle() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());
            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse(applicationjson));
            Call<JsonObject> call = APIClient.getInterface().getVehocle(bodyRequest);
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
                Vehicle vehicle = gson.fromJson(result.toString(), Vehicle.class);
                recyclerviewVehicle.setAdapter(new VehicleAdapter(this, vehicle.getVehicleData(), this,Integer.parseInt(tempPostLoad.getWeight())));

            }

        } catch (Exception e) {
            Log.e("Error","--> "+e.getMessage());

        }
    }


    @OnClick({R.id.img_back, R.id.btn_next})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.btn_next) {
            if(tempPostLoad.getVehicleId()!=null){
                startActivity(new Intent(this, PostLoadPostActivity.class).putExtra("temppost", tempPostLoad).putExtra("edit", loadDetails));
            }else {
                Toast.makeText(PostLoadVevikalActivity.this,getResources().getString(R.string.select_vehicle),Toast.LENGTH_LONG).show();

            }
        }
    }


    @Override
    public void onClickVehicleInfo(VehicleDataItem item, int position) {

        tempPostLoad.setVehicleId(item.getId());
    }


}