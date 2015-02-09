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
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.User;


@Repository("userDAO")
public class UserDAOImpl implements UserDAO {
   
    @Autowired 
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
   

	private Session openSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
	public User getUser(String login) {
        List<User> userList = new ArrayList<User>();
        Query query = openSession().createQuery("from User u where u.email = :email");
        query.setParameter("email", login);
        userList = query.list();
        if (userList.size() > 0) {
        	User user = userList.get(0);
        	Hibernate.initialize(user.getRole());
            return user;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public User getUserByUUID(String uuid) {
    	List<User> userList = new ArrayList<User>();
        Query query = openSession().createQuery("from User u where u.uuid = :uuid");
        query.setParameter("uuid", uuid);
        userList = query.list();
        if (userList.size() > 0) {
        	User user = userList.get(0);
        	Hibernate.initialize(user.getRole());
            return user;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public User getUser(int id) {
        List<User> userList = new ArrayList<User>();
        Query query = openSession().createQuery("from User u where u.id = :id");
        query.setParameter("id", id);
        userList = query.list();
        if (userList.size() > 0) {
        	User user = userList.get(0);
        	Hibernate.initialize(user.getRole());
        	Hibernate.initialize(user.getUserGroups());
            return user;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<User> getUsers() {
        Criteria result = sessionFactory.getCurrentSession().createCriteria(User.class);
        
        return (List<User>)result.list();
    }
    
    @Override
	@Transactional(propagation = Propagation.MANDATORY)
	public int addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
    	
    	return user.getId();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteUser(User user) {
		sessionFactory.getCurrentSession().delete(user);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}
	
	public int getUserUsedCount(int id) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_USER_USED_COUNT).setParameter("id", id);
		return (Integer)query.uniqueResult();
	}
}
