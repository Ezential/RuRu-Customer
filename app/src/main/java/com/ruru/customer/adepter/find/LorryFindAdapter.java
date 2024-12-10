package com.ruru.customer.adepter.find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruru.customer.R;
import com.ruru.customer.adepter.viewholder.LorryViewHolder;
import com.ruru.customer.model.find.LorrydataItem;
import com.ruru.customer.retrofit.APIClient;
import com.ruru.customer.ui.postload.BiderInfoActivity;

import java.util.List;

public class LorryFindAdapter extends RecyclerView.Adapter<LorryViewHolder> {


    private Context mContext;
    private List<LorrydataItem> typeList;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener {
        public void onClickLorryFindInfo(LorrydataItem item, int position);
    }



    public LorryFindAdapter(Context mContext, List<LorrydataItem> typeList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.typeList = typeList;
        this.listener = listener;


    }

    @Override
    public LorryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lorry_layout, parent, false);
        return new LorryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LorryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LorrydataItem item = typeList.get(position);
        holder.txtTrackname.setText(item.getLorryTitle());
        holder.txtLocation.setText(item.getCurrLocation());
        holder.txtVnumber.setText(item.getLorryNo());
        holder.txtWeight.setText("" + item.getWeight() + " " + mContext.getResources().getString(R.string.tonnes));
        holder.txtRouts.setText( item.getRoutesCount()+" "+ mContext.getResources().getString(R.string.routes));
        holder.txtRcVerified.setText(R.string.rc_verified);
        holder.txtRates.setText(item.getReview());

        if (item.getRcVerify().equalsIgnoreCase("1")) {
            holder.txtRcVerified.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_document_done), null, null, null);
        }

        holder.txtName.setText(item.getLorryOwnerTitle());
        Log.e("Imgurl","-->"+APIClient.BASE_URL + "/" + item.getLorryOwnerImg());
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getLorryOwnerImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.imgs);
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getLorryImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.imgTrak);

        String lastWord = item.getCurrLocation().substring(item.getCurrLocation().lastIndexOf(",") + 3);

        Log.e("Location ", "--" + lastWord);

        holder.txtBooknow.setOnClickListener(view -> listener.onClickLorryFindInfo(item, position));

        holder.txtLocation.setOnClickListener(view -> displayAddressWindow(holder.txtLocation, item.getCurrLocation()));

        holder.imgs.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, BiderInfoActivity.class)
                .putExtra("oid", item.getLorryOwnerId())
                .putExtra("lid", item.getLorryId())));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    private void displayAddressWindow(View anchorView, String address) {
        Activity activity = (Activity) mContext;
        PopupWindow popup = new PopupWindow(mContext);
        View layout = activity.getLayoutInflater().inflate(R.layout.popup_content, null);
        TextView textView = layout.findViewById(R.id.txt_address);
        textView.setText("" + address);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.showAsDropDown(anchorView);
    }
}