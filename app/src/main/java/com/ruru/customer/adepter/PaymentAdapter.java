package com.ruru.customer.adepter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruru.customer.R;
import com.ruru.customer.model.PaymentdataItem;
import com.ruru.customer.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {



    private Context mContext;
    private List<PaymentdataItem> typeList;
    private RecyclerTouchListener listener;
    private int lastCheckedPos = -1;


    public interface RecyclerTouchListener {
        public void onClickPaymentInfo(PaymentdataItem item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        public ImageView img;
        @BindView(R.id.txt_pname)
        public TextView txtPname;
        @BindView(R.id.txt_subtitle)
        public TextView txtSubtitle;
        @BindView(R.id.img_select)
        public ImageView imgSelect;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PaymentAdapter(Context mContext, List<PaymentdataItem> typeList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.typeList = typeList;
        this.listener = listener;
      lastCheckedPos = -1;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_layout, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PaymentdataItem item=typeList.get(position);
        if (lastCheckedPos == position) {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.round_boxfill));
            holder.imgSelect.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.radios_fill));
        } else {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.round_box));
            holder.imgSelect.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.radios_empty));

        }
        holder.txtPname.setText(""+item.getTitle());
        holder.txtSubtitle.setText(""+item.getSubtitle());
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.img);

        holder.lvlClick.setOnClickListener(view -> {
            lastCheckedPos = position;
            listener.onClickPaymentInfo(item, position);
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}