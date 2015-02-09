/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.Tag;

@Repository
public class TagDAOImpl implements TagDAO {

	@Autowired
    private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public Tag getTag(int id) {
		List<Tag> result = new ArrayList<Tag>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t where id= :id");
        query.setParameter("id", id);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getSystemTags() {
		List<Tag> result = new ArrayList<Tag>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t");
        result = query.list();

        return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Tag getTag(String name) {
		List<Tag> result = new ArrayList<Tag>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t where name= :name");
        query.setParameter("name", name);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap<Tag, Integer> getCloudTags() {
		HashMap<Tag, Integer> tags = new HashMap<Tag, Integer>();

		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_TAG_CLOUD_QUERY);
		List result = query.list();
		
		Iterator it = result.iterator();
		while ( it.hasNext() ) {
			Object[] objects = (Object[])it.next();
			
			Tag tag = new Tag();
			tag.setName((String)objects[0]);
			
			BigDecimal count = (BigDecimal)objects[1];
			tags.put(tag, count.intValue());
		}
		return tags;
	}
}