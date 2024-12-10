package com.cscodetech.movers.adepter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.movers.R;
import com.google.android.material.imageview.ShapeableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LorryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_trak)
    public ImageView imgTrak;
    @BindView(R.id.txt_trackname)
    public TextView txtTrackname;
    @BindView(R.id.txt_location)
    public TextView txtLocation;
    @BindView(R.id.txt_vnumber)
    public TextView txtVnumber;
    @BindView(R.id.txt_weight)
    public TextView txtWeight;
    @BindView(R.id.txt_routs)
    public TextView txtRouts;
    @BindView(R.id.txt_rc_verified)
    public TextView txtRcVerified;
    @BindView(R.id.imgs)
    public ShapeableImageView imgs;
    @BindView(R.id.txt_name)
    public TextView txtName;
    @BindView(R.id.txt_rates)
    public TextView txtRates;
    @BindView(R.id.txt_booknow)
    public TextView txtBooknow;
    @BindView(R.id.lvl_click)
    public LinearLayout lvlClick;

    public LorryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }
}