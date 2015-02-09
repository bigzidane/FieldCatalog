/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Role;

public interface RoleService {

	public static String ROLE_ADMIN = "ROLE_ADMIN";
	public static String ROLE_DESIGNER = "ROLE_DESIGNER";
	public static String ROLE_VIEWWER = "ROLE_VIEWER";

	public Role getRole(int id);
	
	public List<Role> getRoles();
}
