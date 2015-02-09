/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.itservicesdepot.utils.ApplicationUtils;

@SuppressWarnings("serial")
@MappedSuperclass
public class DateTrackingBase extends BaseModel{
	
	@Column(name = "IS_ACTIVE")
	private String isActive = "Y";
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;
	
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name = "LAST_UPDATED_OWNER_ID")
	private int lastUpdatedUserId;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	public boolean getIsActiveBool() {
		return ApplicationUtils.ny2booleanIndicator(isActive);
	}
	
	public void setIsActiveBool(boolean isActive) {
		this.isActive = ApplicationUtils.bool2YNIndicator(isActive);
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}
	
	public Date getEffectiveDateDisplay() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public Date getExpirationDateDisplay() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}

	public void setLastUpdatedUserId(int lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
}