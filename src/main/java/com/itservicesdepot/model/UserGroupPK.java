/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UserGroupPK implements Serializable {
	private static final long serialVersionUID = 9223077180330327769L;

	@ManyToOne
	private User user;
	
	@ManyToOne
	private Group group;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        UserGroupPK that = (UserGroupPK) o;
 
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (user != null ? user.hashCode() : 0);
        result = 32 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}	
}