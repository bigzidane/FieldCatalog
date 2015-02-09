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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Indexed
@Table(name="PRODUCT", uniqueConstraints={@UniqueConstraint(columnNames="NAME")})
public class Product extends DateTrackingBase {
	private static final long serialVersionUID = 4397634618202844744L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "PRODUCT_ID")
	private List<ProductVersion> productVersion = new ArrayList<ProductVersion>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "PRODUCT_TAG", joinColumns = { 
			@JoinColumn(name = "PRODUCT_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "TAG_ID",nullable = false, updatable = false) })
	private List<Tag> productTags = new ArrayList<Tag>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.product", cascade=CascadeType.ALL)
	private List<ProductCustField> productCustFields = new ArrayList<ProductCustField>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.product", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ProductGroup> productGroups = new ArrayList<ProductGroup>();

	@Column(name = "NAME",unique=true)
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String name;
	
	@Column(name = "DESCRIPTION")
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String description;

	@Column(name = "FILE")
	private String attachedFile;
	
	@Column(name = "OWNER_ID", insertable=false, updatable=false)
	private int ownerId;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;
	
	public List<ProductCustField> getProductCustFields() {
		return productCustFields;
	}
	
	public void setProductCustFields(List<ProductCustField> productCustFields) {
		this.productCustFields = productCustFields;
	}
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof Product)) return false;
		
		Product product = (Product)obj;
		
		if (product.getId() == this.id) return true;
		
		return false;
	}
	/*public List<Version> getProductVersions() {
		return productVersions;
	}
	public void setProductVersions(List<Version> productVersions) {
		this.productVersions = productVersions;
	}*/
	public String getAttachedFile() {
		return attachedFile;
	}
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}
	public List<Tag> getProductTags() {
		return productTags;
	}
	public void setProductTags(List<Tag> productTags) {
		this.productTags = productTags;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public boolean getCustFieldAvailable() {
		if (ValidateUtils.isObjectEmpty(this.getProductCustFields())) return false;
		
		return this.getProductCustFields().size() > 0 ? true : false;
	}

	public List<ProductGroup> getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(List<ProductGroup> productGroups) {
		this.productGroups = productGroups;
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder(71, 81).   
			      append(id).append(name).
			      toHashCode();
	}

	public List<ProductVersion> getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(List<ProductVersion> productVersion) {
		this.productVersion = productVersion;
	}
}