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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Table(name="CUST_FIELD")
public class CustField extends BaseModel{

	private static final long serialVersionUID = 7163220795509711549L;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.custField")
	private List<ProductCustField> targetCustFields = new ArrayList<ProductCustField>();

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "FIELD_ID", nullable=false)
	private String fieldId;
	
	@Column(name = "TARGET", nullable=false)
	private String target;
	
	@Column(name = "FIELD_TYPE", nullable=false)
	private String type;
	
	@Column(name = "FIELD_LABEL", nullable=false)
	private String label;
	
	@Column(name = "FIELD_PREFACE")
	private String preface;
	
	@Column(name = "FIELD_TITLE")
	private String title;
	
	@Column(name = "FIELD_ISREQUIRED")
	private String isRequired;
	
	@Column(name = "FIELD_VALUE")
	private String value;
	
	@Column(name = "FIELD_VALUE_DEF")
	private String valueDef;
	
	@Column(name = "FIELD_ORDER", nullable=false)
	private String order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPreface() {
		return preface;
	}

	public void setPreface(String preface) {
		this.preface = preface;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		if (ValidateUtils.isObjectEmpty(isRequired)) {
			isRequired = "N";
		}
		
		if (isRequired.length() > 1) {
			isRequired = ApplicationUtils.bool2YNIndicator(ApplicationUtils.ny2booleanIndicator(isRequired));
		}
		this.isRequired = isRequired;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getValueDef() {
		return valueDef;
	}

	public void setValueDef(String valueDef) {
		this.valueDef = valueDef;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<ProductCustField> getTargetCustFields() {
		return targetCustFields;
	}

	public void setTargetCustFields(List<ProductCustField> targetCustFields) {
		this.targetCustFields = targetCustFields;
	}
}