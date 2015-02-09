/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import com.itservicesdepot.utils.ValidateUtils;

public class EventValue {

	private String businessRule = "";
	private String codeRule = "";

	public EventValue (String businessRule, String codeRule) {
		this.setBusinessRule(businessRule);
		this.setCodeRule(codeRule );
	}
	
	public EventValue() {
		
	}
	
	public String getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(String businessRule) {
		if (ValidateUtils.isObjectEmpty(businessRule)) businessRule = "";
		this.businessRule = businessRule;
	}

	public String getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(String codeRule) {
		if (ValidateUtils.isObjectEmpty(codeRule)) codeRule = "";
		this.codeRule = codeRule;
	}

}
