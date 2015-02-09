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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.Group;

@Component
public class GroupDAOImpl implements GroupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroups() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Group.class);
		criteria.addOrder(Order.asc("name"));
		
        return (List<Group>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Group getGroup(int id) {
		List<Group> result = new ArrayList<Group>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Group where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Group group = (Group)result.get(0);
        	Hibernate.initialize(group.getUserGroups());
        	
        	return group;
        }
        else {
            return null;
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Group getGroup(String name) {
		List<Group> result = new ArrayList<Group>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Group where name=?").setParameter(0, name);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Group group = (Group)result.get(0);
        	Hibernate.initialize(group.getUserGroups());
        	
        	return group;
        }
        else {
            return null;
        }
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public int addGroup(Group group) {
		sessionFactory.getCurrentSession().save(group);
    	
    	return group.getId();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteGroup(Group group) {
		sessionFactory.getCurrentSession().delete(group);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateGroup(Group group) {
		sessionFactory.getCurrentSession().update(group);
	}

	public int getGroupUsedCount(int id) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_GROUP_USED_COUNT).setParameter("id", id);
		return (Integer)query.uniqueResult();
	}
}
