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
import com.itservicesdepot.model.User;
import com.itservicesdepot.model.UserGroup;
import com.itservicesdepot.service.GroupService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class to handle 2 pages
 * 	1.	Manage Groups
 * 	2.	Add a new Group
 * 
 * Only Admin/Designer can perform Add/Edit/Delete groups
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="groupMgntBean")
@ViewScoped
public class GroupMgntBean extends BaseBean {
	private static final long serialVersionUID = 7767813413467096403L;

	static Logger logger = Logger.getLogger(GroupMgntBean.class);
	
	@ManagedProperty("#{groupService}")
    private GroupService groupService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	// for Manage Grop page
	private List<Group> groups = new ArrayList<Group>();
	
	// for Add Cust Field page
	private List<User> owners;
	private Group group = new Group();
	private DualListModel<User> users;
	
	@PostConstruct
	public void init() {
		this.owners = this.userService.getUsers();
		this.groups = this.groupService.getGroups();
		
		List<User> usersSource = new ArrayList<User>();
		usersSource.addAll(this.owners);
		
        List<User> userTarget = new ArrayList<User>();
        
        users = new DualListModel<User>(usersSource, userTarget);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addGroup() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			this.buildGroupModel(this.group, currentUserId);
			this.buildUserGroupModel(this.group);
			
        	this.getGroupService().addGroup(this.group);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' group is added successfully.", group.getName())));
        	
        	this.reset();
        	
        	return ApplicationConstant.SUCCESS;
        	
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' group is NOT added successfully.. Please contact System Administrator.", group.getName())));
        	
        	logger.error(e.getMessage(), e);
        
        	return ApplicationConstant.ERROR;
        }    
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateGroup(int mode) {
		return updateGroup(this.group, mode);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateGroup(Group group, int mode) {
        try {
        	// mode = 0 -> Lite update
        	if (mode == 1) {
        		buildUserGroupModelEdit(group);
        	}
        	
        	this.getGroupService().updateGroup(group);
        	
        	this.groups = this.getGroupService().getGroups();
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' group is update successfully.", group.getName())));

        	this.reset();
        	
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' group is NOT updated successfully.. Please contact System Administrator.", group.getName())));
        	
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;
        }    
        
    }  
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String deleteGroup(Group group) {
		try {
			Result result = this.getGroupService().deleteGroup(group);
            
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				this.groups = this.getGroupService().getGroups();
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' group is deleted successfully.", group.getName())));
			}
			else if (result.getCode() == ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' group is NOT deleted successfully. The group has been assigned already across the system. Please contact System Administrator.", group.getName())));
			}
                       
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' group is NOT deleted successfully. Please contact System Administrator.", group.getName()));
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;       
        }    
	}
	
	private Group buildGroupModel(Group group, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		group.setCreatedDate(now);
		group.setLastUpdatedDate(now);
		group.setLastUpdatedUserId(currentUserId);
	
		return group;
	}
	
	private void buildUserGroupModel(Group group) {
		List<UserGroup> userGroups = new ArrayList<UserGroup>();

		for (User user : this.users.getTarget()) {
		    UserGroup userGroup = new UserGroup();
		    
		    userGroup.setUser(user);
		    userGroup.setGroup(group);
		    
		    userGroups.add(userGroup);
		}
		
		group.setUserGroups(userGroups);
	}
	
	private void buildUserGroupModelEdit(Group group) {
		List<UserGroup> existingUserGroups = group.getUserGroups();
		List<User> existingUsers = new ArrayList<User>();
		
		for (UserGroup userGroup : existingUserGroups) {
			existingUsers.add(userGroup.getUser());
		}
		
		// add new users if required
		for (User user : this.users.getTarget()) {
			
			if (existingUsers.contains(user)) continue;
			
		    UserGroup userGroup = new UserGroup();
		    
		    userGroup.setUser(user);
		    userGroup.setGroup(group);
		    
		    existingUserGroups.add(userGroup);
		}
		
		// remove users if required
		int i = 0;
		while (i < existingUserGroups.size()) {
			UserGroup userGroup = existingUserGroups.get(i);
			
			if (!this.users.getTarget().contains(userGroup.getUser())) {
				existingUserGroups.remove(i);
			}
			else {
				i++;
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public void updateGroup(RowEditEvent event) {
        updateGroup((Group)event.getObject(), 0);
    }

	public void showGroup(Group group) {
		this.group = this.groupService.getGroup(Integer.valueOf(group.getId()).intValue());
		
		this.users.getTarget().clear();
		this.users.getSource().clear();
		this.users.getSource().addAll(this.owners);
		
		for (UserGroup userGroup : this.group.getUserGroups()) {
			users.getTarget().add(userGroup.getUser());
			users.getSource().remove(userGroup.getUser());
		}
	}
	
	private void reset() {
		if (ValidateUtils.isObjectNotEmpty(this.group)) {
			this.group = new Group();
			this.users.getTarget().clear();
		}
	}
	
	public GroupService getGroupService() {
		return groupService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public DualListModel<User> getUsers() {
		return users;
	}

	public void setUsers(DualListModel<User> users) {
		this.users = users;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
