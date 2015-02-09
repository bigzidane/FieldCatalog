/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itservicesdepot.model.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {
   
    @Autowired
    private SessionFactory sessionFactory;
      
    @SuppressWarnings("unchecked")
	public Role getRole(int id) {
    	List<Role> result = new ArrayList<Role>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Role where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Role role = (Role)result.get(0);
        	
        	return role;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<Role> getRoles() {
    	Criteria result = sessionFactory.getCurrentSession().createCriteria(Role.class);
        
        return (List<Role>)result.list();
    }

}
