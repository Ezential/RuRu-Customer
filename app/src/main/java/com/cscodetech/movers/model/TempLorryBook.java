package com.cscodetech.movers.model;

import java.io.Serializable;

public class TempLorryBook implements Serializable {

    private String uid;
    private String pickupPoint;
    private String dropPoint;
    private String vehicleId;
    private String pickLat;
    private String pickLng;
    private String dropLat;
    private String dropLng;
    private String pickStateId;
    private String lorryId;
    private String lorryOwnerId;
    private String dropStateId;
    private String amount;
    private String amtType;
    private String totalAmt;
    private String title;
    private int lorrywight;

    private String pickname;
    private String pickmobile;
    private String deopname;
    private String dropmobile;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getPickStateId() {
        return pickStateId;
    }

    public void setPickStateId(String pickStateId) {
        this.pickStateId = pickStateId;
    }

    public String getLorryId() {
        return lorryId;
    }

    public void setLorryId(String lorryId) {
        this.lorryId = lorryId;
    }

    public String getLorryOwnerId() {
        return lorryOwnerId;
    }

    public void setLorryOwnerId(String lorryOwnerId) {
        this.lorryOwnerId = lorryOwnerId;
    }

    public String getDropStateId() {
        return dropStateId;
    }

    public void setDropStateId(String dropStateId) {
        this.dropStateId = dropStateId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLorrywight() {
        return lorrywight;
    }

    public void setLorrywight(int lorrywight) {
        this.lorrywight = lorrywight;
    }

    public String getPickname() {
        return pickname;
    }

    public void setPickname(String pickname) {
        this.pickname = pickname;
    }

    public String getPickmobile() {
        return pickmobile;
    }

    public void setPickmobile(String pickmobile) {
        this.pickmobile = pickmobile;
    }

    public String getDeopname() {
        return deopname;
    }

    public void setDeopname(String deopname) {
        this.deopname = deopname;
    }

    public String getDropmobile() {
        return dropmobile;
    }

    public void setDropmobile(String dropmobile) {
        this.dropmobile = dropmobile;
    }
}
