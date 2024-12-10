package com.ruru.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payment{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("paymentdata")
	private List<PaymentdataItem> paymentdata;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public List<PaymentdataItem> getPaymentdata(){
		return paymentdata;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}