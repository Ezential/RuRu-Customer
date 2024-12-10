package com.cscodetech.movers.model.postload;

import com.google.gson.annotations.SerializedName;

public class CheckState{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("drop_state_id")
	private String dropStateId;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("pick_state_id")
	private String pickStateId;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getDropStateId(){
		return dropStateId;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getPickStateId(){
		return pickStateId;
	}

	public String getResult(){
		return result;
	}
}