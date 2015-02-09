/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.Date;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.itservicesdepot.model.User;
import com.itservicesdepot.service.EmailService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.DateTimeUtils;

/**
 * This class handles Login page.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="signupBean")
@RequestScoped
public class SignupBean extends BaseBean{
	private static final long serialVersionUID = 2412984808197531067L;

	static Logger logger = Logger.getLogger(LoginBean.class);
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{emailService}")
    private EmailService emailService;
	
    private User user = new User(); 

	public void signup() {
        try {
        	this.buildUserModel(this.user, 0);
        	this.getUserService().signupUser(this.user);
        	this.sendSignupEmail(this.user);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' user is signed up successfully. Please check your e-mail for the final step.", this.user.getEmail())));

        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' user is NOT signed up successfully.. Please contact System Administrator.", this.user.getEmail())));
            logger.error(e.getMessage(),e);
        }
    }
	
	private User buildUserModel(User user, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		user.setCreatedDate(now);
		user.setLastUpdatedDate(now);
		user.setIsApproved(ApplicationUtils.bool2YNIndicator(false));
		user.setLastUpdatedUserId(currentUserId);
		user.setEffectiveDate(now);
		user.setUuid(UUID.randomUUID().toString());
	
		return user;
	}
	
	private void sendSignupEmail(User user) {
		String activationlink = "<a href='" + ApplicationUtils.getApplicationURL() + "/verifyUser.action?uuid=" + user.getUuid() + "'>Here</a>";
		
        String msg ="Dear "
	                + user.getDisplayName()
	                + ", thank you for signup the product. Please click " + activationlink + " to active your account "; 
        try{
            this.emailService.send("Welcome to Field Catalog online", user.getEmail(), "support@itservicesdepot.com", msg);
        }
        catch(Exception ex) {
            logger.error(ex.getMessage(), ex);            
        }
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
}