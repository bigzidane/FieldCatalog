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
public class ScreenMessagePK implements Serializable {
	private static final long serialVersionUID = -3590075364120847480L;

	@ManyToOne
	private Screen screen;
	
	@ManyToOne
	private Message message;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ScreenMessagePK that = (ScreenMessagePK) o;
 
        if (screen != null ? !screen.equals(that.screen) : that.screen != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (screen != null ? screen.hashCode() : 0);
        result = 101 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}	
}