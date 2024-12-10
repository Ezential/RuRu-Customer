package com.cscodetech.movers.ui.postload;

import android.content.Intent;
import android.os.Bundle;
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
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletPostLoadFragment extends Fragment implements MyLoadPostAdapter.RecyclerTouchListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.lvl_nodata)
    public LinearLayout lvlNodata;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    public static CompletPostLoadFragment newInstance() {
        return new CompletPostLoadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complet_post, container, false);
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
            jsonObject.put("status", "Complete");

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().loadHistory(bodyRequest);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    custPrograssbar.closePrograssBar();

                    Gson gson = new Gson();
                    MyLoad myLoad = gson.fromJson(response.body().toString(), MyLoad.class);
                    if (myLoad.getLoadHistoryData().size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        lvlNodata.setVisibility(View.GONE);
                        MyLoadPostAdapter categoryAdapter = new MyLoadPostAdapter(getActivity(), myLoad.getLoadHistoryData(), CompletPostLoadFragment.this);
                        recyclerView.setAdapter(categoryAdapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        lvlNodata.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    custPrograssbar.closePrograssBar();


                    call.cancel();
                    t.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    int tempPosition = -1;
    @Override
    public void onClickLoadPost(LoadHistoryDataItem item, int position) {

        tempPosition = position;
        startActivity(new Intent(getActivity(), PostLoadDetailsActivity.class).putExtra("lid", item.getId()));

    }
}