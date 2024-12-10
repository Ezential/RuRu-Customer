package com.cscodetech.movers.ui.findlorry;

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
import com.cscodetech.movers.adepter.find.LorryBookAdapter;
import com.cscodetech.movers.model.find.BookLorry;
import com.cscodetech.movers.model.find.LorrydataItem;
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


public class CurrentBookLoadFragment extends Fragment implements LorryBookAdapter.RecyclerTouchListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.lvl_nodata)
    public LinearLayout lvlNodata;

    public static CurrentBookLoadFragment newInstance() {
        return new CurrentBookLoadFragment();
    }

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    public CurrentBookLoadFragment() {
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
        custPrograssbar=new CustPrograssbar();
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
            Call<JsonObject> call = APIClient.getInterface().bookHistory(bodyRequest);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    custPrograssbar.closePrograssBar();

                    Gson gson = new Gson();
                    BookLorry bookLorry = gson.fromJson(response.body().toString(), BookLorry.class);
                    if (bookLorry.getResult().equalsIgnoreCase("true")) {
                        if (bookLorry.getBookHistory().size() != 0) {
                            LorryBookAdapter categoryAdapter = new LorryBookAdapter(getActivity(), bookLorry.getBookHistory(), CurrentBookLoadFragment.this);
                            recyclerView.setAdapter(categoryAdapter);
                            lvlNodata.setVisibility(View.GONE);
                        } else {
                            lvlNodata.setVisibility(View.VISIBLE);
                        }
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



    @Override
    public void onClickLorryInfo(LorrydataItem item, int position) {

        startActivity(new Intent(getActivity(), BookLorryDetailsActivity.class).putExtra("lid",item.getLorryId()));

    }


}