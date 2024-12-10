package com.cscodetech.movers.model.find;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lorry{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("FindLorryData")
	private List<FindLorryDataItem> findLorryData;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public List<FindLorryDataItem> getFindLorryData(){
		return findLorryData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}