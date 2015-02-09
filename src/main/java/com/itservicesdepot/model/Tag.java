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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="TAG")
public class Tag extends BaseModel {
	private static final long serialVersionUID = -7302709577835495299L;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "productTags")
	private List<Product> products = new ArrayList<Product>();
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "screenTags")
	private List<Screen> screens = new ArrayList<Screen>();
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "fieldTags")
	private List<Field> fields = new ArrayList<Field>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false, unique=true)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
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
		
		if (!(obj instanceof Tag)) return false;
		
		Tag tag = (Tag)obj;
		
		if (tag.getId() == this.id) return true;
		if (tag.getName().equalsIgnoreCase(this.getName())) return true;
		
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 39).   
			      append(id).append(name).
			      toHashCode();
	}

	public List<Screen> getScreens() {
		return screens;
	}

	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
}