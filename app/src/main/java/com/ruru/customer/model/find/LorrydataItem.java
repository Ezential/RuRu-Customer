package com.ruru.customer.model.find;

import com.google.gson.annotations.SerializedName;

public class LorrydataItem {

	@SerializedName("lorry_no")
	private String lorryNo;

	@SerializedName("lorry_owner_id")
	private String lorryOwnerId;

	@SerializedName("routes_count")
	private int routesCount;

	@SerializedName("weight")
	private String weight;

	@SerializedName("rc_verify")
	private String rcVerify;

	@SerializedName("lorry_title")
	private String lorryTitle;

	@SerializedName("lorry_img")
	private String lorryImg;

	@SerializedName("lorry_id")
	private String lorryId;

	@SerializedName("review")
	private String review;

	@SerializedName("lorry_owner_title")
	private String lorryOwnerTitle;

	@SerializedName("lorry_owner_img")
	private String lorryOwnerImg;

	@SerializedName("curr_location")
	private String currLocation;

	@SerializedName("vehicle_id")
	private String vehicleId;





	public String getLorryNo(){
		return lorryNo;
	}

	public String getLorryOwnerId(){
		return lorryOwnerId;
	}

	public int getRoutesCount(){
		return routesCount;
	}

	public String getWeight(){
		return weight;
	}

	public String getRcVerify(){
		return rcVerify;
	}

	public String getLorryTitle(){
		return lorryTitle;
	}

	public String getLorryImg(){
		return lorryImg;
	}

	public String getLorryId(){
		return lorryId;
	}

	public String getLorryOwnerTitle() {
		return lorryOwnerTitle;
	}

	public String getLorryOwnerImg() {
		return lorryOwnerImg;
	}

	public String getCurrLocation() {
		return currLocation;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}