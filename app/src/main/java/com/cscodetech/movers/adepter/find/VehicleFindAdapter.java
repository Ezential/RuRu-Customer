package com.cscodetech.movers.adepter.find;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.cscodetech.movers.model.find.FindLorryDataItem;
import com.cscodetech.movers.utils.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleFindAdapter extends RecyclerView.Adapter<VehicleFindAdapter.MyViewHolder> {



    private Context mContext;
    private List<FindLorryDataItem> typeList;
    private RecyclerTouchListener listener;
    private int lastCheckedPos = 0;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickVehicleFindInfo(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_type)
        public TextView txtType;
        @BindView(R.id.txt_tonne)
        public TextView txtTonne;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public VehicleFindAdapter(Context mContext, List<FindLorryDataItem> typeList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.typeList = typeList;
        this.listener = listener;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_type_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (lastCheckedPos == position) {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.rounded_boxfill));
        } else {
            holder.lvlClick.setBackground(mContext.getDrawable(R.drawable.rounded_box));

        }
        holder.txtType.setText("" + typeList.get(position).getTitle());

        holder.txtTonne.setText("" + typeList.get(position).getMinWeight() + " - " + typeList.get(position).getMaxWeight()+" "+ mContext.getResources().getString(R.string.tonnes));

        holder.lvlClick.setOnClickListener(view -> {
            lastCheckedPos=position;
            listener.onClickVehicleFindInfo("", position);
           notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}