/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import java.io.Serializable;

/**
 * This class represents for a base for all Beans across application
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@SuppressWarnings("serial")
public abstract class BaseBean implements Serializable{

	public String actionResult;

	public String getActionResult() {
		return actionResult;
	}

	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}
}