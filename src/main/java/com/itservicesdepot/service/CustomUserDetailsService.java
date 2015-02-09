/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.dao.UserDAO;
import com.itservicesdepot.utils.UserRoleUtils;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
   
    @Autowired
    private UserDAO userDAO;   
    
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
       
        com.itservicesdepot.model.User domainUser = userDAO.getUser(login);
        
        if (domainUser.getIsApprovedBool() && !domainUser.getIsLockedBool()) {
	        Collection<? extends GrantedAuthority> authorities = getAuthorities(domainUser.getRole().getId());
	        boolean enabled = true;
	        boolean accountNonExpired = true;
	        boolean credentialsNonExpired = true;
	        boolean accountNonLocked = true;
	
	        UserDetails userDetails = new User(
	            domainUser.getEmail(),
	            domainUser.getPassword(),
	            enabled,
	            accountNonExpired,
	            credentialsNonExpired,
	            accountNonLocked,
	            authorities
	        );
	        
	        return userDetails;
        }
        else {
        	return null;
        }
    }
   
    public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }
   
    public List<String> getRoles(Integer role) {

        List<String> roles = new ArrayList<String>();

        roles.add(UserRoleUtils.getRoleString(role.intValue()));

        return roles;
    }
   
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
       
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
} 