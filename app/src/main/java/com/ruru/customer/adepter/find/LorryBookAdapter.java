package com.ruru.customer.adepter.find;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruru.customer.R;
import com.ruru.customer.adepter.viewholder.LorryViewHolder;
import com.ruru.customer.model.find.LorrydataItem;
import com.ruru.customer.retrofit.APIClient;

import java.util.List;

public class LorryBookAdapter extends RecyclerView.Adapter<LorryViewHolder> {
    private Context mContext;
    private List<LorrydataItem> lorrydataItemList;
    private RecyclerTouchListener listener;
    public interface RecyclerTouchListener {
        public void onClickLorryInfo(LorrydataItem item, int position);
    }

    public LorryBookAdapter(Context mContext, List<LorrydataItem> typeList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.lorrydataItemList = typeList;
        this.listener = listener;


    }

    @Override
    public LorryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lorry_book_layout, parent, false);
        return new LorryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LorryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LorrydataItem item = lorrydataItemList.get(position);
        holder.txtTrackname.setText(item.getLorryTitle());
        holder.txtLocation.setText(item.getCurrLocation());
        holder.txtVnumber.setText(item.getLorryNo());
        holder.txtWeight.setText("" + item.getWeight() + " " + mContext.getResources().getString(R.string.tonnes));
        holder.txtRouts.setText( item.getRoutesCount()+" "+ mContext.getResources().getString(R.string.routes));
        holder.txtRates.setText(item.getReview());
        holder.txtRcVerified.setText(R.string.rc_verified);
        if (item.getRcVerify().equalsIgnoreCase("1")) {
            holder.txtRcVerified.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_document_done), null, null, null);
        }
        holder.txtName.setText(item.getLorryOwnerTitle());
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getLorryOwnerImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.imgs);
        Glide.with(mContext).load(APIClient.BASE_URL + "/" + item.getLorryImg()).thumbnail(Glide.with(mContext).load(R.drawable.tprofile)).into(holder.imgTrak);
        String lastWord = item.getCurrLocation().substring(item.getCurrLocation().lastIndexOf(",") + 3);
        Log.e("Location ", "--" + lastWord);



        holder.txtBooknow.setOnClickListener(view -> listener.onClickLorryInfo(item, position));
    }

    @Override
    public int getItemCount() {
        return lorrydataItemList.size();
    }
}