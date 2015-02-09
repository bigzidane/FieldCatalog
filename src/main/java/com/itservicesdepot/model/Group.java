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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="SYSTEM_GROUP", uniqueConstraints={@UniqueConstraint(columnNames="NAME")})
public class Group extends DateTrackingBase {
	private static final long serialVersionUID = 5454834825895380935L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.group", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.group", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ProductGroup> productGroups = new ArrayList<ProductGroup>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "NAME",unique=true, nullable = false)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "OWNER_ID", insertable = false, updatable=false)
	private int ownerId;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;

	public int getNumberOfUsers() {
		return this.userGroups.size();
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
		
		if (!(obj instanceof Group)) return false;
		
		Group group = (Group)obj;
		
		if (group.getId() == this.id) return true;
		
		return false;
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
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).   
			      append(id).
			      toHashCode();
	}

	public List<ProductGroup> getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(List<ProductGroup> productGroups) {
		this.productGroups = productGroups;
	}
}