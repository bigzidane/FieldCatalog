/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.itservicesdepot.constant.NavigationConstant;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Screen List page. This page is allowed for all Roles.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="listScreenBean")  
@ViewScoped
public class ListScreenBean extends BaseBean {
	private static final long serialVersionUID = 7102129137003670616L;

	static Logger logger = Logger.getLogger(ListScreenBean.class);
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	private List<Screen> screens;
	
	private User selectedOwner;
	private List<User> owners;
	
	private int paramId;
	
	// for List Product page
	private List<Product> products;					// this is a list of all products available in entire system.
	private List<ProductVersion> allProductVersions;	// this is a list of all productVersions available in entire system.
	
	@PostConstruct
	public void init() {
		String productVersionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(productVersionId)) {
			this.screens = this.screenService.getScreensByProductVersion(productVersionId);
			this.allProductVersions = new ArrayList<ProductVersion>();
			
			ProductVersion pv = this.versionService.getProductVersionOnly(Integer.valueOf(productVersionId));
			this.allProductVersions.add(pv);
			
			this.products = new ArrayList<Product>();
			this.products.add(pv.getProduct());
		}
		else {
			this.screens = this.screenService.getScreens();
			this.products = this.productService.getProducts(); 
			this.allProductVersions = this.versionService.getVersions();
		}
		
		this.owners = userService.getUsers();
	}
	
	public String showScreen(Screen screen) {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_SCREEN_DETAIL;
	}
	
	public String showScreenImage(Screen screen) {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_IMAGE;
	}
	
	public String showScreenMap(Screen screen) {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_SCREEN_MAP;
	}
	
	public String showFields(Screen screen) {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_LIST_FIELDS;
	}
	
	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public List<Screen> getScreens() {
		return screens;
	}

	public void setScreens(List<Screen> screens) {
		this.screens = screens;
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

	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
		this.paramId = paramId;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<ProductVersion> getAllProductVersions() {
		return allProductVersions;
	}

	public void setAllProductVersions(List<ProductVersion> allProductVersions) {
		this.allProductVersions = allProductVersions;
	}
}