package com.cscodetech.movers.model.postload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidStatus implements Parcelable {

    @SerializedName("bidder_id")
    @Expose
    private String bidderId;
    @SerializedName("bidder_name")
    @Expose
    private String bidderName;
    @SerializedName("bidder_img")
    @Expose
    private Object bidderImg;
    @SerializedName("lorry_number")
    @Expose
    private String lorryNumber;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("amt_type")
    @Expose
    private String amtType;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("is_immediate")
    @Expose
    private String isImmediate;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("lorry_id")
    @Expose
    private String lorryid;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("total_lorry")
    @Expose
    private String totalLorry;

    @SerializedName("join_date")
    @Expose
    private String joinDate;

    protected BidStatus(Parcel in) {
        bidderId = in.readString();
        bidderName = in.readString();
        lorryNumber = in.readString();
        amount = in.readString();
        amtType = in.readString();
        totalAmt = in.readString();
        isImmediate = in.readString();
        rate = in.readString();
        lorryid = in.readString();
        description = in.readString();
        totalLorry = in.readString();
        joinDate = in.readString();
    }

    public static final Creator<BidStatus> CREATOR = new Creator<BidStatus>() {
        @Override
        public BidStatus createFromParcel(Parcel in) {
            return new BidStatus(in);
        }

        @Override
        public BidStatus[] newArray(int size) {
            return new BidStatus[size];
        }
    };

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public Object getBidderImg() {
        return bidderImg;
    }

    public void setBidderImg(Object bidderImg) {
        this.bidderImg = bidderImg;
    }

    public String getLorryNumber() {
        return lorryNumber;
    }

    public void setLorryNumber(String lorryNumber) {
        this.lorryNumber = lorryNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getIsImmediate() {
        return isImmediate;
    }

    public void setIsImmediate(String isImmediate) {
        this.isImmediate = isImmediate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLorryid() {
        return lorryid;
    }

    public void setLorryid(String lorryid) {
        this.lorryid = lorryid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalLorry() {
        return totalLorry;
    }

    public String getJoinDate() {
        return joinDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bidderId);
        parcel.writeString(bidderName);
        parcel.writeString(lorryNumber);
        parcel.writeString(amount);
        parcel.writeString(amtType);
        parcel.writeString(totalAmt);
        parcel.writeString(isImmediate);
        parcel.writeString(rate);
        parcel.writeString(lorryid);
        parcel.writeString(description);
        parcel.writeString(joinDate);
        parcel.writeString(totalLorry);
    }
}
