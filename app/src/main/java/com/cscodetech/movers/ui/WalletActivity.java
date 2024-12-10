package com.cscodetech.movers.ui;


import static com.cscodetech.movers.utils.SessionManager.currency;
import static com.cscodetech.movers.utils.SessionManager.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.model.UserLogin;
import com.cscodetech.movers.model.Wallet;
import com.cscodetech.movers.model.WalletitemItem;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class WalletActivity extends BaseActivity implements GetResult.MyListener {


    @BindView(R.id.txt_total)
    TextView txtTotal;

    @BindView(R.id.recy_transaction)
    RecyclerView recyTransaction;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;

    CustPrograssbar custPrograssbar;
    UserLogin user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();

        user = sessionmanager.getUserDetails();
        txtTotal.setText("" + sessionmanager.getStringData(currency) + sessionmanager.getFloatData(wallet));

        recyTransaction.setLayoutManager(new GridLayoutManager(this, 1));
        recyTransaction.setItemAnimator(new DefaultItemAnimator());
        getHistry();

    }

    private void getHistry() {
        custPrograssbar.prograssCreate(WalletActivity.this);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json"));
        Call<JsonObject> call = APIClient.getInterface().walletReport(bodyRequest);
        GetResult getResult = new GetResult(this);
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back, R.id.txt_addmunny})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_addmunny:
             startActivity(new Intent(WalletActivity.this,WalletAddActivity.class));
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
                Wallet walletHistry = gson.fromJson(result.toString(), Wallet.class);
                sessionmanager.setFloatData(wallet, Integer.parseInt(walletHistry.getWalleta()));
                txtTotal.setText("" + sessionmanager.getStringData(currency) + sessionmanager.getFloatData(wallet));
                if (walletHistry.getResult().equalsIgnoreCase("true")) {
                    if (walletHistry.getWalletitem().isEmpty()) {
                        recyTransaction.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                    } else {
                        HistryAdp histryAdp = new HistryAdp(walletHistry.getWalletitem());
                        recyTransaction.setAdapter(histryAdp);
                    }

                } else {
                    recyTransaction.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.toString();

        }

    }

    public class HistryAdp extends RecyclerView.Adapter<HistryAdp.MyViewHolder> {
        private List<WalletitemItem> list;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtDate;
            public TextView txtMessage;

            public TextView txtAmount;

            public MyViewHolder(View view) {
                super(view);
                txtDate = (TextView) view.findViewById(R.id.txt_date);
                txtMessage = (TextView) view.findViewById(R.id.txt_message);

                txtAmount = (TextView) view.findViewById(R.id.txt_amount);
            }
        }

        public HistryAdp(List<WalletitemItem> list) {
            this.list = list;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_histry, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            WalletitemItem category = list.get(position);
            holder.txtDate.setText("" + category.getTdate());
            holder.txtMessage.setText(category.getMessage());

            if (category.getStatus().equalsIgnoreCase("Credit")) {
                holder.txtAmount.setTextColor(getResources().getColor(R.color.green));
                holder.txtAmount.setText("+" + sessionmanager.getStringData(currency) + category.getAmt());
            } else {
                holder.txtAmount.setText("-" + sessionmanager.getStringData(currency) + category.getAmt());
                holder.txtAmount.setTextColor(getResources().getColor(R.color.orange));
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WalletAddActivity.getInstance()!=null && WalletAddActivity.getInstance().walletUp) {
            WalletAddActivity.getInstance().walletUp = false;
            getHistry();
        }

    }
}