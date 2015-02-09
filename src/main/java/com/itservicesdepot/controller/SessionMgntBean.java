/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.UserRoleUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Session.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="sessionMgntBean")
@SessionScoped
public class SessionMgntBean extends BaseBean{
	private static final long serialVersionUID = -626064535768794396L;
	
	static Logger logger = Logger.getLogger(SessionMgntBean.class);
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	private String searchFor;
	private String paramId;
	
	private ProductVersion productVersion;
	
	private User user;
	
	public String globalSearch() {
		this.setParamId(this.searchFor);
		return "successToSearchPage";
	}
	
	@PostConstruct
	public void init() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		this.user = this.userService.getUser(auth.getName());
	}
	
	public String getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean hasSufficientRoles(String requiredRoles) {
		requiredRoles = requiredRoles.toUpperCase();
		if (ValidateUtils.isObjectEmpty(requiredRoles)) return true;
		
		String[] roles = requiredRoles.split(",");
		
		String currentRole = UserRoleUtils.getRoleString(this.user.getRole().getId());
		return (Arrays.asList(roles)).contains(currentRole) ? true : false;
	}
	
	public boolean hasInsufficientRoles(String requiredRoles) {
		return !hasSufficientRoles(requiredRoles);
	}

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
	}
	
	public String getCurrentProductVersionDisplay() {
		if (ValidateUtils.isObjectEmpty(this.productVersion)) {
			return "No active Product Version selected";
		}
		else {
			return String.format("Product: %s - Version: %s", WordUtils.capitalize(this.getCurrentProductName()), this.getCurrentProductVersionName());
		}
	}
	
	public boolean getHasCurrentProductVersion() {
		return (ValidateUtils.isObjectNotEmpty(this.productVersion)) ? true : false;
	}
	
	public String getCurrentProductName() {
		return (ValidateUtils.isObjectNotEmpty(this.productVersion)) ? this.productVersion.getProduct().getName() : "";
	}
	
	public String getCurrentProductVersionName() {
		return (ValidateUtils.isObjectNotEmpty(this.productVersion)) ? this.productVersion.getName() : "";
	}
}