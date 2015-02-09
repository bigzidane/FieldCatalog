/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.CustField;

public interface CustFieldDAO {

	public CustField getCustField(int id);

	public List<CustField> getCustFieldsByProduct();

	public List<CustField> getCustFieldsByScreen();

	public List<CustField> getCustFieldBysField();

	public List<CustField> getCustFieldsByTarget(String target);

	public void addCustField(CustField custField);

	public void deleteCustField(CustField custField);

	public void updateCustField(CustField custField);

	public int getNewDisplayOrder(String target);
	
	public int getCustFieldUsedCount(int id);
	
}
