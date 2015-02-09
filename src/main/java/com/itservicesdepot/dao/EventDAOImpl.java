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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.Event;

@Component
public class EventDAOImpl implements EventDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Event> getEventsByScreen() {
		return getEventsByTarget(ApplicationConstant.SCREEN_TARGET_ID);
	}

	@Override
	public List<Event> getEventBysField() {
		return getEventsByTarget(ApplicationConstant.FIELD_TARGET_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEventsByTarget(String target) {
		List<Event> result = new ArrayList<Event>();
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class);
		criteria.add(Restrictions.eq("target", target));
		criteria.addOrder(Order.asc("order"));
		
		result = criteria.list();

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void addEvent(Event event) {
		sessionFactory.getCurrentSession().save(event);
		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteEvent(Event event) {
		sessionFactory.getCurrentSession().delete(event);
		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateEvent(Event event) {
		sessionFactory.getCurrentSession().update(event);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Event getEvent(int id) {
		List<Event> result = new ArrayList<Event>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from Event where id= :id");
        query.setParameter("id", id);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else {
            return null;
        }
	}
	
	@Override
	public int getNewDisplayOrder(String target) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class);
		criteria.add(Restrictions.eq("target", target));
		criteria.setProjection(Projections.rowCount()).uniqueResult();
		
		return ((Long) criteria.uniqueResult()).intValue(); 
	}
	
	public int getEventUsedCount(int id) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_EVENT_USED_COUNT).setParameter("id", id);
		return (Integer)query.uniqueResult();
	}
}
