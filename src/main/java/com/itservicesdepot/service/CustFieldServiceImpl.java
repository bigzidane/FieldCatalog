/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.CustFieldDAO;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Result;

@Service("custFieldService")
@Transactional(readOnly = true)
public class CustFieldServiceImpl implements CustFieldService {

	@Autowired
	private CustFieldDAO custFieldDAO;

	@Override
	public List<CustField> getCustFieldByProduct() {
		return custFieldDAO.getCustFieldsByProduct();
	}

	@Override
	public List<CustField> getCustFieldByScreen() {
		return custFieldDAO.getCustFieldsByScreen();
	}

	@Override
	public List<CustField> getCustFieldByField() {
		return custFieldDAO.getCustFieldBysField();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result addCustField(CustField custField) {
		Result result = new Result();
		custFieldDAO.addCustField(custField);
		
		return result;

	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result deleteCustField(CustField custField) {
		Result result = new Result();
		
		int usedCount = this.custFieldDAO.getCustFieldUsedCount(custField.getId());
		if (usedCount > 0) {
			result.setCode(ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION);
		}
		else {
			custFieldDAO.deleteCustField(custField);
		}
		
		return result;

	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result updateCustField(CustField custField) {
		Result result = new Result();
		
		custFieldDAO.updateCustField(custField);
		
		return result;
	}

	@Override
	public int getNewDisplayOrder(String target) {
		return custFieldDAO.getNewDisplayOrder(target);
	}

	@Override
	public List<CustField> getCustFieldsByTarget(String target) {
		return custFieldDAO.getCustFieldsByTarget(target);
	}

	@Override
	public CustField getCustField(int id) {
		return custFieldDAO.getCustField(id);
	}
}
