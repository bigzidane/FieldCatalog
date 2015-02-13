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
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Field List page. This page is allowed for all Roles.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="listFieldBean")  
@ViewScoped
public class ListFieldBean extends BaseBean {
	private static final long serialVersionUID = 7102129137003670616L;

	static Logger logger = Logger.getLogger(ListFieldBean.class);
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	private List<Field> fields;
	private List<Field> allFields;
	
	private User selectedOwner;
	private List<User> owners;
	
	private int paramId;
	
	// for List Product page
	private List<Screen> screens;					// this is a list of all screens available in entire system.
	private List<Product> products;					// this is a list of all products available in entire system.
	private List<ProductVersion> allProductVersions;	// this is a list of all productVersions available in entire system.
	
	@PostConstruct
	public void init() {
		String screenId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(screenId)) {
			this.fields = this.fieldService.getFieldsByScreenId(Integer.valueOf(screenId));
			this.allFields = this.fieldService.getUniqueFieldsByScreenId(Integer.valueOf(screenId));
			
			this.screens = new ArrayList<Screen>();
			Screen screen = this.screenService.getScreenOnly(Integer.valueOf(screenId));
			this.screens.add(screen);
			
			this.allProductVersions = new ArrayList<ProductVersion>();
			this.allProductVersions.add(screen.getProductVersion());
			
			this.products = new ArrayList<Product>();
			this.products.add(screen.getProductVersion().getProduct());
		}
		else {
			this.fields = fieldService.getFields();
			this.allFields = this.fieldService.getUniqueFields();
			
			this.screens = this.screenService.getUniqueScreens();
			this.products = this.productService.getProducts(); 
			this.allProductVersions = this.versionService.getUniqueVersions();
		}
		 
		this.owners = userService.getUsers();
	}
	
	public String showField(Field field) {
		this.setParamId(field.getId());
		return NavigationConstant.NAV_TO_FIELD_DETAIL;
	}
	
	public String showAddDocument(Field field) {
		this.setParamId(field.getId());
		return NavigationConstant.NAV_TO_ADD_DOCUMENT;
	}
	
	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
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

	public FieldService getfieldService() {
		return fieldService;
	}

	public void setfieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
		this.paramId = paramId;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
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

	public List<Screen> getScreens() {
		return screens;
	}

	public void setScreens(List<Screen> screens) {
		this.screens = screens;
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

	public List<Field> getAllFields() {
		return allFields;
	}

	public void setAllFields(List<Field> allFields) {
		this.allFields = allFields;
	}

	
}