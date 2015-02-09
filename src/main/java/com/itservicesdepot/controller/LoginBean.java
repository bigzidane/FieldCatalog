/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class handles Login page.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="loginBean")
@RequestScoped
public class LoginBean extends BaseBean{
	private static final long serialVersionUID = 9035544519309853553L;

	static Logger logger = Logger.getLogger(LoginBean.class);
	
    private String userName; 
    private String password;
    private boolean admin = false;

    @ManagedProperty(value="#{authenticationManager}")
    private AuthenticationManager authenticationManager;
    
	public String login() {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), this.getPassword());
            Authentication result = authenticationManager.authenticate(request);
            
            for (GrantedAuthority auth : result.getAuthorities()) {
            	if ("ROLE_ADMIN".equalsIgnoreCase(auth.getAuthority())) {
            		admin = true;
            	}            	
            }
            SecurityContextHolder.getContext().setAuthentication(result);
            
            return "successToMainPage";
        } catch (AuthenticationException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User name or Password is invalid."));
            logger.error(e.getMessage(),e);
            
            return "errorToLoginPage";
        }
        
    }
	
	public String logout(){
        SecurityContextHolder.clearContext();
        return "loggedout";
    }
 
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}