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

@Entity
@Table(name="FIELD_MESSAGE")
@AssociationOverrides({
	@AssociationOverride(name = "pk.screen", 
		joinColumns = @JoinColumn(name = "FIELD_ID")),
	@AssociationOverride(name = "pk.group", 
		joinColumns = @JoinColumn(name = "MESSAGE_ID")) })
public class FieldMessage extends BaseModel {
	private static final long serialVersionUID = 2323368020946698603L;

	@EmbeddedId
	private FieldMessagePK pk = new FieldMessagePK();
	
	@Column(name="IS_ACTIVE")
	private String isActive = "Y";
	
	@Transient
	public Field getField() {
		return getPk().getField();
	}
 
	public void setField(Field screen) {
		getPk().setField(screen);
	}
 
	@Transient
	public Message getMessage() {
		return getPk().getMessage();
	}
 
	public void setMessage(Message category) {
		getPk().setMessage(category);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		FieldMessage that = (FieldMessage) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	
	public FieldMessagePK getPk() {
		return pk;
	}
	public void setPk(FieldMessagePK pk) {
		this.pk = pk;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}