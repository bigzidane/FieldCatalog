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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Indexed
@Table(name="SCREEN", uniqueConstraints={@UniqueConstraint(columnNames="NAME")})
public class Screen extends DateTrackingBase {
	private static final long serialVersionUID = 1056203445681001805L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "SCREEN_TAG", joinColumns = { 
			@JoinColumn(name = "SCREEN_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "TAG_ID",nullable = false, updatable = false) })
	private List<Tag> screenTags = new ArrayList<Tag>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.screen", cascade=CascadeType.ALL)
	private List<ScreenCustField> screenCustFields = new ArrayList<ScreenCustField>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.screen", cascade=CascadeType.ALL)
	private List<ScreenEvent> screenEvents = new ArrayList<ScreenEvent>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.screen", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ScreenGroup> screenGroups = new ArrayList<ScreenGroup>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.screen", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ScreenMessage> screenMessages = new ArrayList<ScreenMessage>();

	@OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="FIELD",
        joinColumns = {@JoinColumn(name="screen_id", referencedColumnName="id")},
        inverseJoinColumns = {@JoinColumn(name="field_id", referencedColumnName="id")}
    )
	private List<Field> fields = new ArrayList<Field>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "SCREEN_ID")
	private List<ScreenDocument> screenDocuments = new ArrayList<ScreenDocument>();
	
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
	
	@Transient
	private String fieldsCount;
	
	@Column(name = "OWNER_ID", insertable=false, updatable=false)
	private int ownerId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PRODUCT_VERSION_ID", nullable = false)
	private ProductVersion productVersion;
	
	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;
	
	@Column(name = "PARENT_ID")
    private String parentId;
	
	public List<ScreenCustField> getScreenCustFields() {
		return screenCustFields;
	}
	
	public void setScreenCustFields(List<ScreenCustField> screenCustFields) {
		this.screenCustFields = screenCustFields;
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
		
		if (!(obj instanceof Screen)) return false;
		
		Screen screen = (Screen)obj;
		
		if (screen.getId() == this.id) return true;
		
		return false;
	}
	public String getAttachedFile() {
		return attachedFile;
	}
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}
	public List<Tag> getScreenTags() {
		return screenTags;
	}
	public void setScreenTags(List<Tag> screenTags) {
		this.screenTags = screenTags;
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
		if (ValidateUtils.isObjectEmpty(this.getScreenCustFields())) return false;
		
		return this.getScreenCustFields().size() > 0 ? true : false;
	}
	
	public boolean getDocumentAvailable() {
		if (ValidateUtils.isObjectEmpty(this.getScreenDocuments())) return false;
		
		return this.getScreenDocuments().size() > 0 ? true : false;
	}

	public List<ScreenGroup> getScreenGroups() {
		return screenGroups;
	}

	public void setScreenGroups(List<ScreenGroup> screenGroups) {
		this.screenGroups = screenGroups;
	}

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(21, 41).   
			      append(id).append(name).
			      toHashCode();
	}

	public List<ScreenEvent> getScreenEvents() {
		return screenEvents;
	}

	public void setScreenEvents(List<ScreenEvent> screenEvents) {
		this.screenEvents = screenEvents;
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

	public List<ScreenMessage> getScreenMessages() {
		return screenMessages;
	}

	public void setScreenMessages(List<ScreenMessage> screenMessages) {
		this.screenMessages = screenMessages;
	}

	public boolean getHasAttachedFile() {
		return (ValidateUtils.isObjectNotEmpty(this.attachedFile)) ? true : false;
	}

	public String getFieldsCount() {
		return fieldsCount;
	}

	public void setFieldsCount(String fieldsCount) {
		this.fieldsCount = fieldsCount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<ScreenDocument> getScreenDocuments() {
		return screenDocuments;
	}

	public void setScreenDocuments(List<ScreenDocument> screenDocuments) {
		this.screenDocuments = screenDocuments;
	}
}