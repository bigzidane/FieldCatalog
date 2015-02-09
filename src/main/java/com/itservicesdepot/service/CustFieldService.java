/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Result;

public interface CustFieldService {
	public CustField getCustField(int id);

	public List<CustField> getCustFieldsByTarget(String target);

	public List<CustField> getCustFieldByProduct();

	public List<CustField> getCustFieldByScreen();

	public List<CustField> getCustFieldByField();

	public Result addCustField(CustField custField);

	public Result deleteCustField(CustField custField);

	public Result updateCustField(CustField custField);

	public int getNewDisplayOrder(String target);
}