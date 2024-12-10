package com.cscodetech.movers.fragment;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static com.cscodetech.movers.utils.SessionManager.currency;
import static com.cscodetech.movers.utils.SessionManager.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.adepter.BannerAdapter;
import com.cscodetech.movers.adepter.OperatAdapter;
import com.cscodetech.movers.model.Home;
import com.cscodetech.movers.model.HomeData;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.ui.HomeActivity;
import com.cscodetech.movers.ui.IdentityVerifyActivity;
import com.cscodetech.movers.ui.NotificationActivity;
import com.cscodetech.movers.ui.findlorry.FindLorryActivity;
import com.cscodetech.movers.ui.postload.PostLoadActivity;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.cscodetech.movers.utils.CustomRecyclerView;
import com.cscodetech.movers.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class HomeFragment extends Fragment implements GetResult.MyListener {


    @BindView(R.id.recycler_banner)
    public CustomRecyclerView recyclerviewBanner;
    @BindView(R.id.recyclerview_operate)
    public RecyclerView recyclerviewOperate;
    @BindView(R.id.lvltop)
    public LinearLayout lvltop;
    @BindView(R.id.lvl_postload)
    public LinearLayout lvlPostload;
    @BindView(R.id.lvl_livelorry)
    public LinearLayout lvlLivelorry;

    @BindView(R.id.txt_user)
    public TextView txtUser;
    @BindView(R.id.txt_isverryfy)
    public TextView txtIsverryfy;
    @BindView(R.id.pullToRefresh)
    public SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.img_profile)
    public ImageView imgProfile;

    @BindView(R.id.nestscroll)
    public NestedScrollView nestscroll;
    SessionManager sessionManager;
    UserLogin userLogin;
    CustPrograssbar custPrograssbar;
    DotsIndicator extensiblePageIndicator;
    HomeData homeData;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        sessionManager.getUserDetails();
        userLogin = sessionManager.getUserDetails();

        txtUser.setText("" + userLogin.getName());
        Glide.with(getActivity()).load(APIClient.BASE_URL + "/" + userLogin.getProPic()).thumbnail(Glide.with(this).load(R.drawable.tprofile)).into(imgProfile);

        recyclerviewOperate.setLayoutManager(new GridLayoutManager(getActivity(), 3, VERTICAL, false));
        recyclerviewOperate.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerviewBanner.setLayoutManager(mLayoutManager1);
        recyclerviewBanner.setItemAnimator(new DefaultItemAnimator());



        pullToRefresh.setOnRefreshListener(() -> {
            getHome(); // your code
            pullToRefresh.setRefreshing(false);
        });
        getHome();




        return view;
    }

    private void getHome() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userLogin.getId());

            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().getHomePage(bodyRequest);
            GetResult getResult = new GetResult(getActivity());
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.lvl_postload, R.id.lvl_livelorry, R.id.img_noti, R.id.img_profile})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_postload:
                if (homeData.getIsVerify().equalsIgnoreCase("2")) {
                    startActivity(new Intent(getActivity(), PostLoadActivity.class));
                } else if (!homeData.getIsVerify().equalsIgnoreCase("1")) {
                    startActivity(new Intent(getActivity(), IdentityVerifyActivity.class).putExtra("isverry", homeData.getIsVerify()));
                } else {
                    Toast.makeText(getActivity(), homeData.getTopMsg(), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.lvl_livelorry:
                if (homeData.getIsVerify().equalsIgnoreCase("2")) {
                    startActivity(new Intent(getActivity(), FindLorryActivity.class));
                } else if (!homeData.getIsVerify().equalsIgnoreCase("1")) {
                    startActivity(new Intent(getActivity(), IdentityVerifyActivity.class).putExtra("isverry", homeData.getIsVerify()));
                } else {
                    Toast.makeText(getActivity(), homeData.getTopMsg(), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.img_noti:

                startActivity(new Intent(getActivity(), NotificationActivity.class));

                break;
            case R.id.img_profile:

                HomeActivity homeActivity = new HomeActivity();
                if (homeActivity.getInstance() != null) {
                    homeActivity.getInstance().profileMenuClick();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Home home = gson.fromJson(result.toString(), Home.class);
                homeData = home.getHomeData();
                switch (homeData.getIsVerify()) {
                    case "0":
                        txtIsverryfy.setCompoundDrawablesWithIntrinsicBounds(null, null, getActivity().getDrawable(R.drawable.ic_document_pending), null);
                        break;
                    case "1":
                        txtIsverryfy.setCompoundDrawablesWithIntrinsicBounds(null, null, getActivity().getDrawable(R.drawable.ic_document_process), null);
                        break;
                    case "2":
                        txtIsverryfy.setCompoundDrawablesWithIntrinsicBounds(null, null, getActivity().getDrawable(R.drawable.ic_document_done), null);
                        break;
                    default:
                        Log.e("error", "ii");
                        break;
                }
                txtIsverryfy.setText("" + homeData.getTopMsg());
                sessionManager.setStringData(currency, home.getHomeData().getCurrency());
                sessionManager.setFloatData(wallet, Float.parseFloat(home.getHomeData().getWallet()));


                BannerAdapter bannerAdp = new BannerAdapter(getActivity(), home.getHomeData().getBanner());
                recyclerviewBanner.setAdapter(bannerAdp);
                recyclerviewBanner.startAutoRotation();

                recyclerviewOperate.setAdapter(new OperatAdapter(getActivity(), home.getHomeData().getStatelist()));
            }

        } catch (Exception e) {
            Log.e("Error", "--> " + e.getMessage());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(IdentityVerifyActivity.getInstance()!=null && IdentityVerifyActivity.getInstance().isUpload){
            getHome();
        }
    }
}

