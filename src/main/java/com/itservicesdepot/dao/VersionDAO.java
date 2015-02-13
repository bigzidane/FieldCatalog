/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.ProductVersionDocument;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenVersion;

public interface VersionDAO {

	public List<ProductVersion> getUniqueVersions();
	
	public ProductVersion getProductVersionOnly(int id);
	
	public List<ProductVersion> getProductVersions();
	
	public ProductVersion getProductVersion(int id);
	
	public ScreenVersion getScreenVersion(int id);
	
	public List<ProductVersion> getProductVersionsByProductId(int productId);
	
	public List<ScreenVersion> getScreenVersionsByScreenId(int screenId);
	
	public List<Screen> getScreensByProductVersionId(int productVersionId);
	
	public ProductVersion getProductVersion(String productName, String productVersionName);
	
	public int addProductVersion(ProductVersion productVersion);
	
	public int cloneProductVersion(ProductVersion productVersion, int actorId);
	
	public ProductVersionDocument getDocument(String name, int screenId);

	public int addDocument(ProductVersionDocument document);
}
