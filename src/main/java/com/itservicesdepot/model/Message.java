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
@Table(name="SYSTEM_MESSAGE", uniqueConstraints={@UniqueConstraint(columnNames="CODE")})
public class Message extends DateTrackingBase {
	private static final long serialVersionUID = 5035294543678480521L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.message", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ScreenMessage> screenMessages = new ArrayList<ScreenMessage>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.message", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FieldMessage> fieldMessages = new ArrayList<FieldMessage>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "CODE",unique=true, nullable = false)
	private String code;
	
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "OWNER_ID", insertable = false, updatable=false)
	private int ownerId;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof Message)) return false;
		
		Message group = (Message)obj;
		
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(25, 45).   
			      append(id).
			      toHashCode();
	}
	public List<ScreenMessage> getScreenMessages() {
		return screenMessages;
	}
	public void setScreenMessages(List<ScreenMessage> screenMessages) {
		this.screenMessages = screenMessages;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}
	public void setFieldMessages(List<FieldMessage> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}
}