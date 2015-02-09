/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.User;

public interface UserService {

	public User getUser(String login);

	public User getUser(int login);
	
	public User getUserByUUID(String uuid);

	public List<User> getUsers();

	public Result addUser(User user);
	
	public Result signupUser(User user);
	
	public Result updateUser(User user);
	
	public Result deleteUser(User user);
}
