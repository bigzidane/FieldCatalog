/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.User;

public interface UserDAO {

	public User getUser(String login);

	public User getUser(int id);
	
	public User getUserByUUID(String uuid);

	public List<User> getUsers();
	
	public int addUser(User user);
	
	public void updateUser(User user);
	
	public void deleteUser(User user);
	
	public int getUserUsedCount(int id);

}
