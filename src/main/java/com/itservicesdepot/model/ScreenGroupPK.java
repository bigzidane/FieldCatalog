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
public class ScreenGroupPK implements Serializable {
	private static final long serialVersionUID = 7792220149139397983L;

	@ManyToOne
	private Screen screen;
	
	@ManyToOne
	private Group group;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ScreenGroupPK that = (ScreenGroupPK) o;
 
        if (screen != null ? !screen.equals(that.screen) : that.screen != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (screen != null ? screen.hashCode() : 0);
        result = 100 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}	
}