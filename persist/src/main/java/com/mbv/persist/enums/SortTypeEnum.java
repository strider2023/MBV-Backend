package com.mbv.persist.enums;

public enum SortTypeEnum {

	lastUpdateDate("createdDate", "create_ts"),
	startDate("lastUpdatedDate", "update_ts"),
    status("status","status");

	String fieldName;
	
	String hqlFieldName;
	
	SortTypeEnum(String fieldName, String hqlFieldName){
		this.fieldName = fieldName;
		this.hqlFieldName = hqlFieldName;
	}
	
	public String getFieldName(){
		return this.fieldName;
	}
	
	public String getHqlFieldName(){
		return this.hqlFieldName;
	}
}
