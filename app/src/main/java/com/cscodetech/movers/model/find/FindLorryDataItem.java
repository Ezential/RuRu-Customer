package com.cscodetech.movers.model.find;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FindLorryDataItem{

	@SerializedName("max_weight")
	private String maxWeight;

	@SerializedName("lorrydata")
	private List<LorrydataItem> lorrydata;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("min_weight")
	private String minWeight;

	public String getMaxWeight(){
		return maxWeight;
	}

	public List<LorrydataItem> getLorrydata(){
		return lorrydata;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getMinWeight(){
		return minWeight;
	}
}