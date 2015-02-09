/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.itservicesdepot.utils.ValidateUtils;

@Entity
@Table(name="FIELD_EVENT")
@AssociationOverrides({
	@AssociationOverride(name = "pk.field", 
		joinColumns = @JoinColumn(name = "FIELD_ID")),
	@AssociationOverride(name = "pk.event", 
		joinColumns = @JoinColumn(name = "EVENT_ID")) })
public class FieldEvent extends BaseModel {
	private static final long serialVersionUID = -7386110066400925491L;

	@EmbeddedId
	private FieldEventPK pk = new FieldEventPK();
	
	@Column(name="Business_Rule")
	private String businessRule;
	
	@Column(name="Code_Rule")
	private String codeRule;
	
	@Transient
	public Field getField() {
		return getPk().getField();
	}
 
	public void setField(Field field) {
		getPk().setField(field);
	}
 
	@Transient
	public Event getEvent() {
		return getPk().getEvent();
	}
 
	public void setEvent(Event category) {
		getPk().setEvent(category);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		FieldEvent that = (FieldEvent) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	public FieldEventPK getPk() {
		return pk;
	}
	public void setPk(FieldEventPK pk) {
		this.pk = pk;
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
	
	public void setEventValue(EventValue value) {
		if (ValidateUtils.isObjectEmpty(value)) {
			this.businessRule = "";
			this.codeRule = "";
		}
		else {
			this.businessRule = value.getBusinessRule();
			this.codeRule = value.getCodeRule();
		}
	}
}