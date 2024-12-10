package com.cscodetech.movers.ui.postload;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.MyLoadPostAdapter;
import com.cscodetech.movers.model.postload.LoadHistoryDataItem;
import com.cscodetech.movers.model.postload.MyLoad;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.SessionManager;
import com.cscodetech.movers.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class CurrentPostLoadFragment extends Fragment implements MyLoadPostAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.lvl_nodata)
    public LinearLayout lvlNodata;

    public static CurrentPostLoadFragment newInstance() {
        return new CurrentPostLoadFragment();
    }

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    public CurrentPostLoadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_postload, container, false);
        ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoad();
        return view;
    }

    private void getLoad() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails().getId());
            jsonObject.put("status", "Current");

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().loadHistory(bodyRequest);
            GetResult getResult = new GetResult(getActivity());
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MyLoadPostAdapter categoryAdapter;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                MyLoad myLoad = gson.fromJson(result.toString(), MyLoad.class);
                if (myLoad.getLoadHistoryData().size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    lvlNodata.setVisibility(View.GONE);
                    categoryAdapter = new MyLoadPostAdapter(getActivity(), myLoad.getLoadHistoryData(), this);
                    recyclerView.setAdapter(categoryAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    lvlNodata.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Log.e("Error","-"+e.getMessage());
        }

    }

    int tempPosition = -1;

    @Override
    public void onClickLoadPost(LoadHistoryDataItem item, int position) {
        tempPosition = position;
        startActivity(new Intent(getActivity(), PostLoadDetailsActivity.class).putExtra("lid", item.getId()));

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Utility.getInstance().removepost) {
                categoryAdapter.getItemList().remove(tempPosition);
                this.categoryAdapter.notifyDataSetChanged();
                Utility.getInstance().removepost = false;
            }

        } catch (Exception e) {
            Log.e("Error","-"+e.getMessage());

        }

    }
}