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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.constant.NavigationConstant;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;


/**
 * This class represents for a Product Version
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="productVersionBean")
@ViewScoped
public class ProductVersionBean extends BaseBean {
	private static final long serialVersionUID = -6701547499259690855L;
	
	static Logger logger = Logger.getLogger(ProductVersionBean.class);

	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	private Product product;
	private List<Product> products;
	
	private ProductVersion newProductVersion = new ProductVersion();
	private ProductVersion productVersion;	
	private List<ProductVersion> productVersions;				
	
	@PostConstruct
	public void init() {
		this.products = this.productService.getProducts(); 
	}
	
	public void onProductChange() {
		if (ValidateUtils.isObjectNotEmpty(this.product)) {
			this.productVersions = this.versionService.getProductVersionsByProductId(this.product.getId());
		}
		else {
			this.productVersions = new ArrayList<ProductVersion>();
		}
    }
	
	public String addProductVersion() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			newProductVersion.setProduct(this.product);
			
			if (ValidateUtils.isObjectNotEmpty(this.productVersion)) {
				newProductVersion.setCloneFromVersionId(this.productVersion.getId());
			}
			
			Result result = this.versionService.addProductVersion(newProductVersion, currentUserId);
			
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The product '%s' version '%s' is added successfully. Please contact System Administrator.", this.product.getName(), newProductVersion.getName())));
				
				return NavigationConstant.NAV_TO_LIST_PRODUCTS;
			}
			else if (result.getCode() == ErrorCodeConstant.DUPLICATE_ENTRY) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' product and version '%s' already exists.", this.newProductVersion.getProduct().getName(), this.newProductVersion.getName())));
			}
			else if (result.getCode() == ErrorCodeConstant.ERROR_ADD_ENTRY_NAME_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("Version '%s' should NOT lower than existings one.", this.newProductVersion.getProduct().getName(), this.newProductVersion.getName())));
			}
			return "";
		}
		catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The product version is NOT added successfully. Please contact System Administrator.")));
			logger.error(ex.getMessage(), ex);
			return "";
		}
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public ProductVersion getNewProductVersion() {
		return newProductVersion;
	}

	public void setNewProductVersion(ProductVersion newProductVersion) {
		this.newProductVersion = newProductVersion;
	}

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
	}

	public List<ProductVersion> getProductVersions() {
		return productVersions;
	}

	public void setProductVersions(List<ProductVersion> productVersions) {
		this.productVersions = productVersions;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}