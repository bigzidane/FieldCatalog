/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Role;
import com.itservicesdepot.model.User;
import com.itservicesdepot.model.UserGroup;
import com.itservicesdepot.service.GroupService;
import com.itservicesdepot.service.RoleService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class to handle 2 pages
 * 	1.	Manage Users
 * 	2.	Add a new User
 * 
 * Only Admin/Designer can perform Add/Edit/Delete users
   * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="userMgntBean")
@ViewScoped
public class UserMgntBean extends BaseBean {
	private static final long serialVersionUID = -2929978138887138822L;

	static Logger logger = Logger.getLogger(UserMgntBean.class);
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{groupService}")
    private GroupService groupService;
	
	@ManagedProperty("#{roleService}")
    private RoleService roleService;
	
	// for Manage User page
	private List<User> users = new ArrayList<User>();
	
	// for Add User page
	private List<Role> roles;
	private User user = new User();
	private Group group;
	private DualListModel<Group> groups;
	private List<Group> existingGroups;
	
	@PostConstruct
	public void init() {
		this.users = this.userService.getUsers();
		this.roles = this.roleService.getRoles();
		this.existingGroups = this.groupService.getGroups();
		
		List<Group> groupsSource = new ArrayList<Group>();
		groupsSource.addAll(existingGroups);
        List<Group> groupsTarget = new ArrayList<Group>();
        
        groups = new DualListModel<Group>(groupsSource, groupsTarget);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addUser() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			this.buildUserModel(this.user, currentUserId);
			this.buildUserGroupModel(this.user);
			
        	this.getUserService().addUser(this.user);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' user is added successfully.", this.user.getEmail())));
        	
        	this.reset();
        	
        	return ApplicationConstant.SUCCESS;
        	
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' user is NOT added successfully.. Please contact System Administrator.", this.user.getEmail())));
        	
        	logger.error(e.getMessage(), e);
        
        	return ApplicationConstant.ERROR;
        }    
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateUser() {
		return updateUser(this.user);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateUser(User user) {
        try {
       		buildUserGroupModel(user);
        	
        	this.getUserService().updateUser(user);
        	
        	this.users = this.getUserService().getUsers();
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' user is update successfully.", this.user.getEmail())));

        	this.reset();
        	
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' user is NOT updated successfully.. Please contact System Administrator.", this.user.getEmail())));
        	
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;
        }    
        
    }  
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String deleteUser(User user) {
		try {
			Result result = this.getUserService().deleteUser(user);
            
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				this.users = this.getUserService().getUsers();
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' user is deleted successfully.", this.user.getEmail())));
			}
			else if (result.getCode() == ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' user is NOT deleted successfully. The user has been assigned already across the system. Please contact System Administrator.", user.getEmail())));
			}
			
			return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' user is NOT deleted successfully. Please contact System Administrator.", this.user.getEmail())));
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;       
        }    
	}
	
	private User buildUserModel(User user, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		user.setCreatedDate(now);
		user.setLastUpdatedDate(now);
		user.setLastUpdatedUserId(currentUserId);
	
		return user;
	}
	
	private void buildUserGroupModel(User user) {
		List<UserGroup> userGroups = new ArrayList<UserGroup>();

		for (Group group : this.groups.getTarget()) {
		    UserGroup userGroup= new UserGroup();
		    
		    userGroup.setUser(user);
		    userGroup.setGroup(group);
		    
		    userGroups.add(userGroup);
		}
		
		user.setUserGroups(userGroups);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public void updateUser(RowEditEvent event) {
        updateUser((User)event.getObject());
    }

	public void showUser(User user) {
		this.user = this.userService.getUser(Integer.valueOf(user.getId()).intValue());
		
		this.groups.getTarget().clear();
		this.groups.getSource().clear();
		this.groups.getSource().addAll(this.existingGroups);
		
		for (UserGroup userGroup : this.user.getUserGroups()) {
			groups.getTarget().add(userGroup.getGroup());
			groups.getSource().remove(userGroup.getGroup());
		}
	}
	
	private void reset() {
		if (ValidateUtils.isObjectNotEmpty(this.user)) {
			this.user = new User();
			this.groups.getTarget().clear();
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

	public DualListModel<Group> getGroups() {
		return groups;
	}

	public void setGroups(DualListModel<Group> groups) {
		this.groups = groups;
	}

}
