package com.ruru.customer.model.postload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoadDetails implements Parcelable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vehicle_title")
    @Expose
    private String vehicleTitle;
    @SerializedName("vehicle_img")
    @Expose
    private String vehicleImg;
    @SerializedName("pickup_point")
    @Expose
    private String pickupPoint;
    @SerializedName("drop_point")
    @Expose
    private String dropPoint;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pick_lat")
    @Expose
    private String pickLat;
    @SerializedName("pick_lng")
    @Expose
    private String pickLng;
    @SerializedName("drop_lat")
    @Expose
    private String dropLat;
    @SerializedName("drop_lng")
    @Expose
    private String dropLng;
    @SerializedName("drop_state_id")
    @Expose
    private String dropStateId;
    @SerializedName("visible_hours")
    @Expose
    private String visibleHours;
    @SerializedName("pick_state_id")
    @Expose
    private String pickStateId;
    @SerializedName("pickup_state")
    @Expose
    private String pickupState;
    @SerializedName("drop_state")
    @Expose
    private String dropState;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("amt_type")
    @Expose
    private String amtType;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("svisible_hours")
    @Expose
    private Integer svisibleHours;
    @SerializedName("hours_type")
    @Expose
    private String hoursType;
    @SerializedName("material_name")
    @Expose
    private String materialName;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("load_status")
    @Expose
    private String loadStatus;

    @SerializedName("p_method_name")
    @Expose
    private String pMethodName;

    @SerializedName("Order_Transaction_id")
    @Expose
    private String orderTransactionId;

    @SerializedName("wal_amt")
    @Expose
    private String walAmt;

    @SerializedName("bidder_id")
    @Expose
    private String bidderId;

    @SerializedName("lorry_id")
    @Expose
    private String lorryIid;

    @SerializedName("bidder_name")
    @Expose
    private String bidderName;

    @SerializedName("bidder_img")
    @Expose
    private String bidderImg;

    @SerializedName("lorry_number")
    @Expose
    private String lorryNumber;

    @SerializedName("bidder_mobile")
    @Expose
    private String bidderMobile;

    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("offer_description")
    @Expose
    private String offerDescription;

    @SerializedName("offer_price")
    @Expose
    private String offerPrice;

    @SerializedName("offer_by")
    @Expose
    private String offerBy;

    @SerializedName("offer_type")
    @Expose
    private String offerType;

    @SerializedName("offer_total")
    @Expose
    private String offerTotal;

    @SerializedName("flow_id")
    @Expose
    private String flowId;

    @SerializedName("comment_reject")
    @Expose
    private String commentReject;

    @SerializedName("pay_amt")
    @Expose
    private String payAmt;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("is_rate")
    @Expose
    private String isRate;

    @SerializedName("pick_name")
    @Expose
    private String pickName;

    @SerializedName("pick_mobile")
    @Expose
    private String pickMobile;

    @SerializedName("drop_name")
    @Expose
    private String dropName;

    @SerializedName("drop_mobile")
    @Expose
    private String dropMobile;

    @SerializedName("bid_status")
    @Expose
    private List<BidStatus> bidStatus;


    protected LoadDetails(Parcel in) {
        id = in.readString();
        vehicleTitle = in.readString();
        vehicleImg = in.readString();
        pickupPoint = in.readString();
        dropPoint = in.readString();
        description = in.readString();
        pickLat = in.readString();
        pickLng = in.readString();
        dropLat = in.readString();
        dropLng = in.readString();
        dropStateId = in.readString();
        visibleHours = in.readString();
        pickStateId = in.readString();
        pickupState = in.readString();
        dropState = in.readString();
        amount = in.readString();
        amtType = in.readString();
        totalAmt = in.readString();
        postDate = in.readString();
        if (in.readByte() == 0) {
            svisibleHours = null;
        } else {
            svisibleHours = in.readInt();
        }
        hoursType = in.readString();
        materialName = in.readString();
        weight = in.readString();
        loadStatus = in.readString();
        pMethodName = in.readString();
        orderTransactionId = in.readString();
        walAmt = in.readString();
        bidderId = in.readString();
        lorryIid = in.readString();
        bidderName = in.readString();
        bidderImg = in.readString();
        lorryNumber = in.readString();
        bidderMobile = in.readString();
        rate = in.readString();
        offerDescription = in.readString();
        offerPrice = in.readString();
        offerBy = in.readString();
        offerType = in.readString();
        offerTotal = in.readString();
        flowId = in.readString();
        commentReject = in.readString();
        payAmt = in.readString();
        message = in.readString();
        isRate = in.readString();
        pickName = in.readString();
        pickMobile = in.readString();
        dropName= in.readString();
        dropMobile= in.readString();
        bidStatus = in.createTypedArrayList(BidStatus.CREATOR);
    }

    public static final Creator<LoadDetails> CREATOR = new Creator<LoadDetails>() {
        @Override
        public LoadDetails createFromParcel(Parcel in) {
            return new LoadDetails(in);
        }

        @Override
        public LoadDetails[] newArray(int size) {
            return new LoadDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleTitle() {
        return vehicleTitle;
    }

    public void setVehicleTitle(String vehicleTitle) {
        this.vehicleTitle = vehicleTitle;
    }

    public String getVehicleImg() {
        return vehicleImg;
    }

    public void setVehicleImg(String vehicleImg) {
        this.vehicleImg = vehicleImg;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickLat() {
        return pickLat;
    }

    public void setPickLat(String pickLat) {
        this.pickLat = pickLat;
    }

    public String getPickLng() {
        return pickLng;
    }

    public void setPickLng(String pickLng) {
        this.pickLng = pickLng;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLng() {
        return dropLng;
    }

    public void setDropLng(String dropLng) {
        this.dropLng = dropLng;
    }

    public String getDropStateId() {
        return dropStateId;
    }

    public void setDropStateId(String dropStateId) {
        this.dropStateId = dropStateId;
    }

    public String getVisibleHours() {
        return visibleHours;
    }

    public void setVisibleHours(String visibleHours) {
        this.visibleHours = visibleHours;
    }

    public String getPickStateId() {
        return pickStateId;
    }

    public void setPickStateId(String pickStateId) {
        this.pickStateId = pickStateId;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public String getDropState() {
        return dropState;
    }

    public void setDropState(String dropState) {
        this.dropState = dropState;
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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Integer getSvisibleHours() {
        return svisibleHours;
    }

    public void setSvisibleHours(Integer svisibleHours) {
        this.svisibleHours = svisibleHours;
    }

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public List<BidStatus> getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(List<BidStatus> bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getpMethodName() {
        return pMethodName;
    }

    public String getOrderTransactionId() {
        return orderTransactionId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public String getLorryIid() {
        return lorryIid;
    }

    public String getBidderName() {
        return bidderName;
    }

    public String getBidderImg() {
        return bidderImg;
    }

    public String getLorryNumber() {
        return lorryNumber;
    }

    public String getBidderMobile() {
        return bidderMobile;
    }

    public String getWalAmt() {
        return walAmt;
    }

    public String getRate() {
        return rate;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public String getOfferBy() {
        return offerBy;
    }

    public String getOfferType() {
        return offerType;
    }

    public String getOfferTotal() {
        return offerTotal;
    }

    public String getFlowId() {
        return flowId;
    }

    public String getCommentReject() {
        return commentReject;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public String getMessage() {
        return message;
    }

    public String getIsRate() {
        return isRate;
    }

    public String getPickName() {
        return pickName;
    }

    public String getPickMobile() {
        return pickMobile;
    }

    public String getDropName() {
        return dropName;
    }

    public String getDropMobile() {
        return dropMobile;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(vehicleTitle);
        parcel.writeString(vehicleImg);
        parcel.writeString(pickupPoint);
        parcel.writeString(dropPoint);
        parcel.writeString(description);
        parcel.writeString(pickLat);
        parcel.writeString(pickLng);
        parcel.writeString(dropLat);
        parcel.writeString(dropLng);
        parcel.writeString(dropStateId);
        parcel.writeString(visibleHours);
        parcel.writeString(pickStateId);
        parcel.writeString(pickupState);
        parcel.writeString(dropState);
        parcel.writeString(amount);
        parcel.writeString(amtType);
        parcel.writeString(totalAmt);
        parcel.writeString(postDate);
        if (svisibleHours == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(svisibleHours);
        }
        parcel.writeString(hoursType);
        parcel.writeString(materialName);
        parcel.writeString(weight);
        parcel.writeString(loadStatus);
        parcel.writeString(pMethodName);
        parcel.writeString(orderTransactionId);
        parcel.writeString(walAmt);
        parcel.writeString(bidderId);
        parcel.writeString(lorryIid);
        parcel.writeString(bidderName);
        parcel.writeString(bidderImg);
        parcel.writeString(lorryNumber);
        parcel.writeString(bidderMobile);
        parcel.writeString(rate);
        parcel.writeString(offerDescription);
        parcel.writeString(offerPrice);
        parcel.writeString(offerBy);
        parcel.writeString(offerType);
        parcel.writeString(offerTotal);
        parcel.writeString(flowId);
        parcel.writeString(commentReject);
        parcel.writeString(payAmt);
        parcel.writeString(message);
        parcel.writeString(isRate);
        parcel.writeString(pickName);
        parcel.writeString(pickMobile);
        parcel.writeString(dropName);
        parcel.writeString(dropMobile);
        parcel.writeTypedList(bidStatus);
    }
}