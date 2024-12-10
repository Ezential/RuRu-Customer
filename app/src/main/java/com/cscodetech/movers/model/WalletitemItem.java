package com.cscodetech.movers.model;

import com.google.gson.annotations.SerializedName;

public class WalletitemItem{

	@SerializedName("tdate")
	private String tdate;

	@SerializedName("amt")
	private String amt;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getTdate(){
		return tdate;
	}

	public String getAmt(){
		return amt;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}