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
import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Result;

@Component
public class MessageDAOImpl implements MessageDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessages() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteria.addOrder(Order.asc("code"));
		
        return (List<Message>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Message getMessage(int id) {
		List<Message> result = new ArrayList<Message>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Message where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Message message = (Message)result.get(0);

        	Hibernate.initialize(message.getScreenMessages());
        	Hibernate.initialize(message.getFieldMessages());
        	
        	return message;
        }
        else {
            return null;
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message getMessage(String code) {
		List<Message> result = new ArrayList<Message>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Message where code=?").setParameter(0, code);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Message message = (Message)result.get(0);
        	
        	Hibernate.initialize(message.getScreenMessages());
        	Hibernate.initialize(message.getFieldMessages());
        	
        	return message;
        }
        else {
            return null;
        }
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Result addMessage(Message message) {
		Result result = new Result();
		
		sessionFactory.getCurrentSession().save(message);
    	
    	result.setId(message.getId());
    	
    	return result;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Result deleteMessage(Message message) {
		Result result = new Result();
		
		sessionFactory.getCurrentSession().delete(message);
		
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Result updateMessage(Message message) {
		Result result = new Result();
		
		sessionFactory.getCurrentSession().update(message);
		
		return result;
	}
	
	public int getMessageUsedCount(int id) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_MESSAGE_USED_COUNT).setParameter("id", id);
		return (Integer)query.uniqueResult();
	}
}
