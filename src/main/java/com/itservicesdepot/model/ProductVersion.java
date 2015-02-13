/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.text.WordUtils;

import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Table(name="PRODUCT_VERSION")
public class ProductVersion extends BaseModel {
	private static final long serialVersionUID = -8027086134333827335L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product = new Product();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productVersion", cascade=CascadeType.ALL)
	private List<Screen> screen = new ArrayList<Screen>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "PRODUCT_VERSION_ID")
	private List<ProductVersionDocument> productVersionDocuments = new ArrayList<ProductVersionDocument>();
	
	@Column(name="isActive", nullable = true)
	private String isActive = "Y";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;

	@Column(name = "NAME", nullable = false, unique=true)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Transient
	private int cloneFromVersionId = 0;

	@Transient
	private int screensCount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		ProductVersion that = (ProductVersion) o;
 
		if (that.getId() != this.getId())
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return new HashCodeBuilder(1, 1).   
			      append(id).
			      toHashCode();
	}
	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Screen> getScreen() {
		return screen;
	}

	public void setScreen(List<Screen> screen) {
		this.screen = screen;
	}
	
	public String getDisplayName() {
		if (ValidateUtils.isObjectNotEmpty(this.product)) {
			return WordUtils.capitalize(this.product.getName() + " " + this.name);
		}
		else {
			return this.name;
		}
	}

	public boolean getDocumentAvailable() {
		return ValidateUtils.isObjectEmpty(this.getProductVersionDocuments()) ? false : true;
	}
	
	public int getCloneFromVersionId() {
		return cloneFromVersionId;
	}

	public void setCloneFromVersionId(int cloneFromVersionId) {
		this.cloneFromVersionId = cloneFromVersionId;
	}
	
	public int getScreensCount() {
		return screensCount;
	}

	public void setScreensCount(int screensCount) {
		this.screensCount = screensCount;
	}

	public List<ProductVersionDocument> getProductVersionDocuments() {
		return productVersionDocuments;
	}

	public void setProductVersionDocuments(
			List<ProductVersionDocument> productVersionDocuments) {
		this.productVersionDocuments = productVersionDocuments;
	}
}