/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.extensions.component.masterdetail.SelectLevelEvent;

  
@ManagedBean
@RequestScoped  
public class ListGroupLevelListener {  
  
    private boolean errorOccured = false;  
  
    public int handleNavigation(SelectLevelEvent selectLevelEvent) {  
        if (errorOccured) {  
            return 2;  
        } else {  
            return selectLevelEvent.getNewLevel();  
        }  
    }  
  
    public void setErrorOccured(boolean errorOccured) {  
        this.errorOccured = errorOccured;  
    }  
} 