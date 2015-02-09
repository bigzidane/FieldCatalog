/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.text.WordUtils;

@Entity
@Table(name="ROLE")
public class Role extends BaseModel {
	private static final long serialVersionUID = 6205310223951549666L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
   
	@Column(name = "role")
    private String role;
   
    
    public Role() {
		super();
	}
    
    public Role(int id, String role) {
    	this.id = id;
    	this.role = role;
    }

	@OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="USER_ROLE",
        joinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")},
        inverseJoinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")}
    )    
    private Set<User> userRoles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<User> userRoles) {
        this.userRoles = userRoles;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof Role)) return false;
		
		Role role = (Role)obj;
		
		if (role.getId() == this.id) return true;
		
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(63, 83).   
			      append(id).
			      toHashCode();
	}
	
	public String getDisplayName() {
		return WordUtils.capitalize(this.role.toLowerCase());
	}
   
}