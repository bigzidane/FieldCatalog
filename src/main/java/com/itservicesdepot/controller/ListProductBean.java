/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.itservicesdepot.constant.NavigationConstant;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;

/**
 * This class handles Product List page. This page is allowed for all Roles.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="listProductBean")  
@ViewScoped
public class ListProductBean extends BaseBean {
	private static final long serialVersionUID = -6838984719155266394L;

	static Logger logger = Logger.getLogger(ListProductBean.class);
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{sessionMgntBean}")
	private SessionMgntBean sessionMgntBean;
	
	private List<ProductVersion> productVersions;
	
	private User selectedOwner;
	private List<User> owners;
	
	private int paramId;
	
	private List<Product> products;
	private List<ProductVersion> allProductVersions;	// this is a list of all productVersions available in entire system.
	
	@PostConstruct
	public void init() {
		this.products = this.productService.getProducts();
		this.productVersions = this.versionService.getProductVersions(); 
		this.allProductVersions = this.versionService.getUniqueVersions();
		
		this.owners = userService.getUsers();
	}
	
	public String showProduct(ProductVersion productVersion) {
		this.setParamId(productVersion.getId());
		return NavigationConstant.NAV_TO_PRODUCT_DETAIL;
	}
	
	public String showAddDocument(ProductVersion productVersion) {
		this.setParamId(productVersion.getId());
		return NavigationConstant.NAV_TO_ADD_DOCUMENT;
	}
	
	public String showProductMap(ProductVersion productVersion) {
		this.setParamId(productVersion.getId());
		return NavigationConstant.NAV_TO_PRODUCT_MAP;
	}
	
	public String showScreens(ProductVersion productVersion) {
		this.setParamId(productVersion.getId());
		return NavigationConstant.NAV_TO_LIST_SCREENS;
	}
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public User getSelectedOwner() {
		return selectedOwner;
	}

	public void setSelectedOwner(User selectedOwner) {
		this.selectedOwner = selectedOwner;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public SessionMgntBean getSessionMgntBean() {
		return sessionMgntBean;
	}

	public void setSessionMgntBean(SessionMgntBean sessionMgntBean) {
		this.sessionMgntBean = sessionMgntBean;
	}

	public List<ProductVersion> getAllProductVersions() {
		return allProductVersions;
	}

	public void setAllProductVersions(List<ProductVersion> allProductVersions) {
		this.allProductVersions = allProductVersions;
	}

	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
		this.paramId = paramId;
	}

	public List<ProductVersion> getProductVersions() {
		return productVersions;
	}

	public void setProductVersions(List<ProductVersion> productVersions) {
		this.productVersions = productVersions;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}