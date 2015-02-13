/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.io.Serializable;

import com.itservicesdepot.constant.ErrorCodeConstant;

public class Result implements Serializable {
	private static final long serialVersionUID = -1683455791664164861L;
	
	private int id;
	
	private int code = ErrorCodeConstant.SUCCESS;
	
	private String message;
	
	private String extraInfo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return ErrorCodeConstant.SUCCESS == this.code ? true : false;
	}
	
	public boolean isError() {
		return !this.isSuccess();
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
}