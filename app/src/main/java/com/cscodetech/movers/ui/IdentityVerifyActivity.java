package com.cscodetech.movers.ui;

import static com.cscodetech.movers.utils.FileUtils.createPartFromString;
import static com.cscodetech.movers.utils.FileUtils.prepareFilePart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.movers.R;
import com.cscodetech.movers.model.RestResponse;
import com.cscodetech.movers.retrofit.APIClient;
import com.cscodetech.movers.retrofit.GetResult;
import com.cscodetech.movers.utils.CustPrograssbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class IdentityVerifyActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.img_isuploadp)
    public ImageView imgIsuploadp;
    @BindView(R.id.lvl_passport)
    public LinearLayout lvlPassport;
    @BindView(R.id.img_isuploadi)
    public ImageView imgIsuploadi;
    @BindView(R.id.lvl_selfe)
    public LinearLayout lvlSelfe;
    @BindView(R.id.txt_subtitle)
    public TextView txtSubtitle;
    @BindView(R.id.txt_subtitle1)
    public TextView txtSubtitle1;
    @BindView(R.id.btn_upload)
    public TextView btnUpload;
    @BindView(R.id.crd_selfe)
    public CardView lvlSelfec;
    @BindView(R.id.lvl_identy)
    public CardView lvlIdenty;
    @BindView(R.id.recyclerselectimg)
    public RecyclerView recyclerselectimg;

    ArrayList<Imagemode> arrayListImage = new ArrayList<>();

    CustPrograssbar custPrograssbar;

    String isVerry;

    public static IdentityVerifyActivity identityVerifyActivity;

    public static IdentityVerifyActivity getInstance() {
        return identityVerifyActivity;
    }

    public boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verify);
        ButterKnife.bind(this);
        identityVerifyActivity = this;
        isVerry = getIntent().getStringExtra("isverry");

        if (isVerry.equalsIgnoreCase("4")) {
            lvlSelfec.setVisibility(View.GONE);
        } else if (isVerry.equalsIgnoreCase("5")) {
            lvlIdenty.setVisibility(View.GONE);
        }

        custPrograssbar = new CustPrograssbar();


        recyclerselectimg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerselectimg.setItemAnimator(new DefaultItemAnimator());


    }

    @OnClick({R.id.img_back, R.id.btn_upload})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_upload:
                uploadMultiFile();
                break;

            default:
                break;

        }


    }


    private void uploadMultiFile() {
        custPrograssbar.prograssCreate(IdentityVerifyActivity.this);

        List<MultipartBody.Part> parts = new ArrayList<>();
        List<MultipartBody.Part> partss = new ArrayList<>();
        String status = "";

        for (int i = 0; i < arrayListImage.size(); i++) {
            if (arrayListImage.get(i).getType().equalsIgnoreCase("doc")) {
                parts.add(prepareFilePart("image" + i, arrayListImage.get(i).getImg()));
            } else {
                partss.add(prepareFilePart("images" + 0, arrayListImage.get(i).getImg()));
            }
        }
        status = getStatus(isVerry);

        RequestBody uid = createPartFromString(sessionmanager.getUserDetails().getId());
        RequestBody statuss = createPartFromString(status);
        RequestBody size = createPartFromString("" + parts.size());
        RequestBody sizes = createPartFromString("" + partss.size());

        Call<JsonObject> call = APIClient.getInterface().dyAnswer(uid, statuss, size, parts, sizes, partss);
        GetResult getResult = new GetResult(this);
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    private String getStatus(String isVerry) {
        String status = "";
        switch (isVerry) {
            case "0":
                status = "First";
                break;
            case "3":
                status = "Both";
                break;
            case "4":
                status = "Identity";
                break;
            case "5":
                status = "Selfie";
                break;
            default:
                break;
        }
        return status;
    }

    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            Gson gson = new Gson();
            RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
            Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
            if (response.getResult().equalsIgnoreCase("true")) {
                isUpload = true;
                finish();

            }
        } catch (Exception e) {
            Log.e("Error", "--> " + e.getMessage());

        }
    }


    public void postImage(List<Imagemode> urilist) {
        ImageAdp imageAdp = new ImageAdp(urilist);
        recyclerselectimg.setAdapter(imageAdp);
        imgeselect(urilist);

    }

    public void imgeselect(List<Imagemode> urilist) {
        int doc = 0;
        int self = 0;
        for (int i = 0; i < urilist.size(); i++) {
            if (urilist.get(i).getType().equalsIgnoreCase("doc")) {
                doc = doc + 1;
            } else if (urilist.get(i).getType().equalsIgnoreCase("self")) {
                self = self + 1;
            }
        }

        if (doc == 2) {
            imgIsuploadp.setVisibility(View.VISIBLE);
            txtSubtitle.setText(getString(R.string.uploaded_successfully));
        } else {
            imgIsuploadp.setVisibility(View.GONE);
            txtSubtitle.setText(getString(R.string.haven_t_uploaded_yet));


        }
        if (self == 1) {
            txtSubtitle1.setText(getString(R.string.uploaded_successfully));
            imgIsuploadi.setVisibility(View.VISIBLE);
        } else {
            imgIsuploadi.setVisibility(View.GONE);
            txtSubtitle1.setText(getString(R.string.haven_t_uploaded_yet));

        }

        if ((self == 1 && doc == 2) || (doc == 2 && isVerry.equalsIgnoreCase("4")) || (self == 1 && isVerry.equalsIgnoreCase("5"))) {
            btnUpload.setVisibility(View.VISIBLE);
        } else {
            btnUpload.setVisibility(View.GONE);

        }

    }


    public class ImageAdp extends RecyclerView.Adapter<ImageAdp.MyViewHolder> {
        private List<Imagemode> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView remove;
            private ImageView thumbnail;
            private TextView txtType;

            public MyViewHolder(View view) {
                super(view);

                thumbnail = view.findViewById(R.id.image_pic);
                remove = view.findViewById(R.id.image_remove);
                txtType = view.findViewById(R.id.txt_type);
            }
        }

        public ImageAdp(List<Imagemode> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            Glide.with(IdentityVerifyActivity.this)
                    .load(arrayList.get(position).img)
                    .thumbnail(Glide.with(IdentityVerifyActivity.this).load(R.drawable.tprofile))
                    .into(holder.thumbnail);
            holder.txtType.setText(arrayList.get(position).type);
            holder.remove.setOnClickListener(v -> {

                if (!arrayList.isEmpty()) {
                    arrayList.remove(position);
                    notifyDataSetChanged();
                    imgeselect(arrayList);
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    class Imagemode {
        String type;
        String img;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}