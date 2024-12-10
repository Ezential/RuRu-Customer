package com.cscodetech.movers.adepter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.model.postload.VehicleDataItem;
import com.cscodetech.movers.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {


    private Context mContext;
    int tonne;
    private List<VehicleDataItem> typeList;
    private RecyclerTouchListener listener;
    private int lastCheckedPos = -1;


    public interface RecyclerTouchListener {
        public void onClickVehicleInfo(VehicleDataItem item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        public ImageView img;
        @BindView(R.id.txt_name)
        public TextView txtName;
        @BindView(R.id.txt_type)
        public TextView txtType;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public VehicleAdapter(Context mContext, List<VehicleDataItem> typeList, final RecyclerTouchListener listener, int tonne) {
        this.mContext = mContext;
        this.typeList = typeList;
        this.listener = listener;
        this.tonne = tonne;
        lastCheckedPos = -1;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        VehicleDataItem item = typeList.get(position);
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.img);
        holder.txtName.setText(item.getTitle());
        holder.txtType.setText(item.getMinWeight() + " - " + item.getMaxWeight() + " Tonnes");

        if (lastCheckedPos == position) {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.round_boxfill));
        } else {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.round_box));
        }
        holder.lvlClick.setOnClickListener(view -> {
            if (tonne < Integer.parseInt(item.getMaxWeight())) {
                lastCheckedPos = position;

                listener.onClickVehicleInfo(item, position);
                notifyDataSetChanged();
            }else {
                Toast.makeText(mContext,"It is not possible to choose a lorry with a your load capacity.",Toast.LENGTH_LONG).show();
            }


        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}