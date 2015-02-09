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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.VersionDAO;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenVersion;
import com.itservicesdepot.utils.ApplicationUtils;

@Service("versionService")
@Transactional(readOnly = true)
public class VersionServiceImpl implements VersionService {
	
	@Autowired
    private VersionDAO versionDAO;

	public List<ProductVersion> getVersions() {
		return versionDAO.getVersions();
	}
	
	public ProductVersion getProductVersionOnly(int id) {
		return versionDAO.getProductVersionOnly(id);
	}
	
	public ProductVersion getProductVersion(int id) {
		return versionDAO.getProductVersion(id);
	}
	
	public ScreenVersion getScreenVersion(int id) {
		return versionDAO.getScreenVersion(id);
	}
	
	public List<ProductVersion> getProductVersionsByProductId(int productId) {
		return versionDAO.getProductVersionsByProductId(productId);
	}
	
	public List<ScreenVersion> getScreenVersionsByScreenId(int screenId)  {
		return versionDAO.getScreenVersionsByScreenId(screenId);
	}
	
	public List<Screen> getScreensByProductVersionId(int productVersionId) {
		return versionDAO.getScreensByProductVersionId(productVersionId);
	}
	
	public ProductVersion getProductVersion(String productName, String productVersionName) {
		return versionDAO.getProductVersion(productName, productVersionName);
	}
	
	public VersionDAO getVersionDAO() {
		return versionDAO;
	}

	public void setVersionDAO(VersionDAO versionDAO) {
		this.versionDAO = versionDAO;
	}

	@Override
	@Transactional(readOnly = false)
	public Result addProductVersion(ProductVersion productVersion, int actorId) {
		Result result = new Result();
		
		List<ProductVersion> productVersions = this.versionDAO.getProductVersionsByProductId(productVersion.getProduct().getId());
        for (ProductVersion existingVersion : productVersions) {
        	if (productVersion.getName().equalsIgnoreCase(existingVersion.getName())) {
        		result.setCode(ErrorCodeConstant.DUPLICATE_ENTRY);
        		return result;
        	}
        	else {
        		int vc = ApplicationUtils.versionCompare(productVersion.getName(), existingVersion.getName());
        		if (vc <=0 ) {
        			result.setCode(ErrorCodeConstant.ERROR_ADD_ENTRY_NAME_VIOLATION);
        			return result;
        		}
        	}
        }
        
		if (productVersion.getCloneFromVersionId() == 0) {
			this.versionDAO.addProductVersion(productVersion);
		}
		else {
			this.versionDAO.cloneProductVersion(productVersion, actorId);
		}
		
		return result;
		
	}

	@Override
	public List<ProductVersion> getProductVersions() {
		return this.versionDAO.getProductVersions();
	}
}