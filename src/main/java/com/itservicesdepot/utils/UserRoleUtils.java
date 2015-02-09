/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.utils;

import com.itservicesdepot.service.RoleServiceImpl;

public class UserRoleUtils {
	
	public static String getRoleString(int id) {
    	String role = RoleServiceImpl.ROLE_VIEWWER;
    	switch (id) {
    	case 1:
    		role =RoleServiceImpl.ROLE_ADMIN;
    		break;
    	case 2:
    		role =RoleServiceImpl.ROLE_DESIGNER;
    		break;
    	case 3:
    		role =RoleServiceImpl.ROLE_VIEWWER;
    		break;
    	}
    	
    	return role;
	}
}