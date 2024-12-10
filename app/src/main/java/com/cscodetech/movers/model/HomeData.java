package com.cscodetech.movers.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData{

	@SerializedName("wallet")
	private String wallet;

	@SerializedName("Banner")
	private List<BannerItem> banner;

	@SerializedName("Statelist")
	private List<StatelistItem> statelist;

	@SerializedName("currency")
	private String currency;

	@SerializedName("is_verify")
	private String isVerify;

	@SerializedName("top_msg")
	private String topMsg;

	public String getWallet(){
		return wallet;
	}

	public List<BannerItem> getBanner(){
		return banner;
	}

	public List<StatelistItem> getStatelist(){
		return statelist;
	}

	public String getCurrency(){
		return currency;
	}

	public String getIsVerify() {
		return isVerify;
	}

	public String getTopMsg() {
		return topMsg;
	}
}