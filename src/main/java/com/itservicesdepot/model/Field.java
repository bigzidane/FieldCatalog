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
import javax.persistence.Embedded;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.itservicesdepot.model.fieldtag.FieldTagInfo;
import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Indexed
@Table(name="FIELD", uniqueConstraints={@UniqueConstraint(columnNames="NAME")})
public class Field extends DateTrackingBase {
	private static final long serialVersionUID = 4178329863428998189L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "FIELD_TAG", joinColumns = { 
			@JoinColumn(name = "FIELD_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "TAG_ID",nullable = false, updatable = false) })
	private List<Tag> fieldTags = new ArrayList<Tag>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.field", cascade=CascadeType.ALL)
	private List<FieldCustField> fieldCustFields = new ArrayList<FieldCustField>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.field", cascade=CascadeType.ALL)
	private List<FieldEvent> fieldEvents = new ArrayList<FieldEvent>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.field", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.field", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FieldMessage> fieldMessages = new ArrayList<FieldMessage>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "FIELD_ID")
	private List<FieldDocument> fieldDocuments = new ArrayList<FieldDocument>();

	@Column(name = "NAME",unique=true)
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String name;
	
	@Column(name = "DESCRIPTION")
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String description;

	@Column(name = "BUSINESS_RULE")
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String businessRule;
	
	@Column(name = "CODE_RULE")
	@org.hibernate.search.annotations.Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String codeRule;
	
	@Column(name = "FILE")
	private String attachedFile;
	
	@Column(name = "OWNER_ID", insertable=false, updatable=false)
	private int ownerId;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "SCREEN_ID", nullable = false)
	private Screen screen;
	
	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;
	
	@Column(name = "DEPENDENT_ID")
    private String dependentId;
	
	@Embedded
    private FieldTagInfo tagInfo = new FieldTagInfo();
	
	public List<FieldCustField> getFieldCustFields() {
		return fieldCustFields;
	}
	
	public void setFieldCustFields(List<FieldCustField> fieldCustFields) {
		this.fieldCustFields = fieldCustFields;
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
		if (!(obj instanceof Field)) return false;
		
		Field field = (Field)obj;
		
		if (field.getId() == this.id) return true;
		
		return false;
	}
	public String getAttachedFile() {
		return attachedFile;
	}
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}
	public List<Tag> getFieldTags() {
		return fieldTags;
	}
	public void setFieldTags(List<Tag> fieldTags) {
		this.fieldTags = fieldTags;
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
		if (ValidateUtils.isObjectEmpty(this.getFieldCustFields())) return false;
		
		return this.getFieldCustFields().size() > 0 ? true : false;
	}
	
	public boolean getDocumentAvailable() {
		return ValidateUtils.isObjectEmpty(this.getFieldDocuments()) ? false : true;
	}

	public List<FieldGroup> getFieldGroups() {
		return fieldGroups;
	}

	public void setFieldGroups(List<FieldGroup> fieldGroups) {
		this.fieldGroups = fieldGroups;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 39).   
			      append(id).append(name).
			      toHashCode();
	}

	public List<FieldEvent> getFieldEvents() {
		return fieldEvents;
	}

	public void setFieldEvents(List<FieldEvent> fieldEvents) {
		this.fieldEvents = fieldEvents;
	}

	public String getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(String businessRule) {
		this.businessRule = businessRule;
	}

	public String getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}

	public List<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}

	public void setFieldMessages(List<FieldMessage> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}

	public FieldTagInfo getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(FieldTagInfo tagInfo) {
		this.tagInfo = tagInfo;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public String getDependentId() {
		return dependentId;
	}

	public void setDependentId(String dependentId) {
		this.dependentId = dependentId;
	}

	public List<FieldDocument> getFieldDocuments() {
		return fieldDocuments;
	}

	public void setFieldDocuments(List<FieldDocument> fieldDocuments) {
		this.fieldDocuments = fieldDocuments;
	}
}