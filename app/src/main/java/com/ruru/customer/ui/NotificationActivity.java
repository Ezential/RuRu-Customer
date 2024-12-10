package com.ruru.customer.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ruru.customer.R;
import com.ruru.customer.model.Noti;
import com.ruru.customer.model.NotificationDatum;
import com.ruru.customer.model.UserLogin;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.retrofit.GetResult;
import com.ruru.customer.utils.CustPrograssbar;
import com.ruru.customer.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class NotificationActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.lvl_myorder)
    LinearLayout lvlMyOrder;

    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    UserLogin user;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        user = sessionmanager.getUserDetails();
        custPrograssbar = new CustPrograssbar();
        getNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void setNotiList(LinearLayout lnrView, List<NotificationDatum> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);

            View view = inflater.inflate(R.layout.custome_noti, null);

            TextView txtTitel = view.findViewById(R.id.txt_titel);
            TextView txtDatetime = view.findViewById(R.id.txt_datetime);
            TextView txtDesc = view.findViewById(R.id.txt_desc);

            String date = Utility.getInstance().parseDateToddMMyy(list.get(i).getDate());

            txtTitel.setText("" + list.get(i).getMsg() + "  ");
            txtDesc.setText("" + list.get(i).getDescription());
            txtDatetime.setText("" + date);

            lnrView.addView(view);

        }

    }



    private void getNotification() {
        custPrograssbar.prograssCreate(NotificationActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
            Call<JsonObject> call = APIClient.getInterface().getNote(bodyRequest);
            GetResult getResult = new GetResult(this);
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Noti notification = gson.fromJson(result.toString(), Noti.class);
                if (notification.getNotificationData().size()!=0) {

                    setNotiList(lvlMyOrder, notification.getNotificationData());
                } else {
                    lvlMyOrder.setVisibility(View.GONE);
                    lvlNotFound.setVisibility(View.VISIBLE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}