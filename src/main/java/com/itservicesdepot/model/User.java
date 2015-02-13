/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.text.WordUtils;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.utils.ApplicationUtils;

@Entity
@Table(name="USER")
public class User extends DateTrackingBase {
	private static final long serialVersionUID = 7755181403667670466L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
   
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "FIRST_NAME")
    private String firstName;
    
    @Column(name = "MIDDLE_NAME") 
    private String middleName;
    
    @Column(name = "LAST_NAME")
    private String lastName;
    
    @Column(name = "IS_APPROVED")
    private String isApproved;
    
    @Column(name = "IS_LOCKED")
    private String isLocked;
    
    @Column(name = "PHONE")
    private String phone;
    
    @Column(name = "COMPANY")
    private String company;
    
    @Column(name = "EMPLOYEES")
    private String employees;
    
    @Column(name = "UUID")
    private String uuid;
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="USER_ROLE",
        joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
        inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}
    )
    private Role role;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
    
    @Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof User)) return false;
		
		User user = (User)obj;
		
		if (user.getId() == this.id) return true;
		
		return false;
	}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getDisplayName() {
		return WordUtils.capitalize(String.format("%s %s", this.firstName, this.lastName));
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(53, 73).   
			      append(id).
			      toHashCode();
	}
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
	public boolean getIsLockedBool() {
		return ApplicationUtils.ny2booleanIndicator(this.isLocked);
	}
	
	public void setIsLockedBool(boolean isLocked) {
		this.isLocked = ApplicationUtils.bool2YNIndicator(isLocked);
	}
	
	public boolean getIsApprovedBool() {
		return ApplicationUtils.ny2booleanIndicator(this.isApproved);
	}
	
	public void setIsApprovedBool(boolean isApproved) {
		this.isApproved = ApplicationUtils.bool2YNIndicator(isApproved);
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmployees() {
		return employees;
	}
	public void setEmployees(String employees) {
		this.employees = employees;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean isSysAdmin() {
		return this.email.startsWith(ApplicationConstant.SYS_ADMIN) ? true : false;
	}
}
