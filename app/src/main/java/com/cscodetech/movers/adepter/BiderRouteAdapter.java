package com.cscodetech.movers.adepter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BiderRouteAdapter extends RecyclerView.Adapter<BiderRouteAdapter.MyViewHolder> {



    private Context mContext;
    private List<String> typeList;






    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        public ImageView img;
        @BindView(R.id.txt_name)
        public TextView txtName;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public BiderRouteAdapter(Context mContext, List<String> typeList) {
        this.mContext = mContext;
        this.typeList = typeList;



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bider_route_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String[] templ=typeList.get(position).split(",");
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + templ[1]).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.img);
        holder.txtName.setText(""+templ[0]);



    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}