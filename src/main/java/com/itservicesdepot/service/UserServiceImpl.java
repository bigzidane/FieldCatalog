/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.UserDAO;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.User;

@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
   
    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUser(String login) {
        return userDAO.getUser(login);
    }
    
    @Override
    public User getUserByUUID(String uuid) {
        return userDAO.getUserByUUID(uuid);
    }
    
    public User getUser(int id) {
        return userDAO.getUser(id);
    }
    
    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    @Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
    public Result addUser(User user) {
    	Result result = new Result();
    	
    	result.setId(userDAO.addUser(user));
    	
    	return result;
    }
    
    @Override
	@Transactional(readOnly = false)
    public Result signupUser(User user) {
    	Result result = new Result();
    	
    	result.setId(userDAO.addUser(user));
    	
    	return result;
    }
	
    @Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result updateUser(User user) {
    	Result result = new Result();
    	
		userDAO.updateUser(user);
		
		return result;
	}
	
    @Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result deleteUser(User user) {
    	Result result = new Result();
    	
    	int usedCount = this.userDAO.getUserUsedCount(user.getId());
		if (usedCount > 0) {
			result.setCode(ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION);
		}
		else {
			userDAO.deleteUser(user);
		}
		
		return result;
	}
    
}
