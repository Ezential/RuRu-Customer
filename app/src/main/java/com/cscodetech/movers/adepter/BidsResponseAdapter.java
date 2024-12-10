package com.cscodetech.movers.adepter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.model.postload.BidStatus;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.ui.postload.BiderInfoActivity;
import com.cscodetech.movers.utils.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BidsResponseAdapter extends RecyclerView.Adapter<BidsResponseAdapter.MyViewHolder> {


    private Context mContext;
    private List<BidStatus> typeList;
    private RecyclerTouchListener listener;

    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickBidsInfo(BidStatus item, int position);
        public void onClickBidsInfoReject(BidStatus item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgs)
        public ShapeableImageView imgs;
        @BindView(R.id.txt_name)
        public TextView txtName;
        @BindView(R.id.txt_price)
        public TextView txtPrice;
        @BindView(R.id.txt_rates)
        public TextView txtRates;
        @BindView(R.id.txt_reject)
        public TextView txtReject;
        @BindView(R.id.txt_accept)
        public TextView txtAccept;
        @BindView(R.id.txt_lorrynumber)
        public TextView txtLorrynumber;
        @BindView(R.id.txt_atype)
        public TextView txtAtype;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public BidsResponseAdapter(Context mContext, List<BidStatus> typeList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.typeList = typeList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bidresponse_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BidStatus bidStatus = typeList.get(position);
        holder.txtName.setText("" + bidStatus.getBidderName());
        holder.txtRates.setText(""+ bidStatus.getRate());
        holder.txtLorrynumber.setText(""+ bidStatus.getLorryNumber());

        holder.txtAtype.setText("/ "+ bidStatus.getAmtType());
        holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + bidStatus.getAmount());

        Glide.with(mContext).load(APIClient.BASE_URL + "/" + bidStatus.getBidderImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.imgs);

        holder.txtReject.setOnClickListener(view -> {

            listener.onClickBidsInfoReject(typeList.get(position), position);
            notifyDataSetChanged();

        });

        holder.txtAccept.setOnClickListener(view -> {

            listener.onClickBidsInfo(typeList.get(position), position);
            notifyDataSetChanged();

        });
        holder.imgs.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, BiderInfoActivity.class)
                .putExtra("oid",typeList.get(position).getBidderId())
                .putExtra("lid",typeList.get(position).getLorryid())));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}