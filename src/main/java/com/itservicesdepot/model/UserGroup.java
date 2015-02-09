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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="USER_GROUP")
@AssociationOverrides({
	@AssociationOverride(name = "pk.user", 
		joinColumns = @JoinColumn(name = "USER_ID")),
	@AssociationOverride(name = "pk.group", 
		joinColumns = @JoinColumn(name = "GROUP_ID")) })
public class UserGroup extends BaseModel {
	private static final long serialVersionUID = 5934769765039908208L;

	@EmbeddedId
	private UserGroupPK pk = new UserGroupPK();
	
	@Transient
	public User getUser() {
		return getPk().getUser();
	}
 
	public void setUser(User product) {
		getPk().setUser(product);
	}
 
	@Transient
	public Group getGroup() {
		return getPk().getGroup();
	}
 
	public void setGroup(Group category) {
		getPk().setGroup(category);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		UserGroup that = (UserGroup) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	public UserGroupPK getPk() {
		return pk;
	}
	public void setPk(UserGroupPK pk) {
		this.pk = pk;
	}
}