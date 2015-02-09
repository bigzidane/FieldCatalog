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
import com.itservicesdepot.model.CustField;

@Component
public class CustFieldDAOImpl implements CustFieldDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	@Override
	public List<CustField> getCustFieldsByProduct() {
		return getCustFieldsByTarget(ApplicationConstant.PRODUCT_TARGET_ID);
	}

	@Override
	public List<CustField> getCustFieldsByScreen() {
		return getCustFieldsByTarget(ApplicationConstant.SCREEN_TARGET_ID);
	}

	@Override
	public List<CustField> getCustFieldBysField() {
		return getCustFieldsByTarget(ApplicationConstant.FIELD_TARGET_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustField> getCustFieldsByTarget(String target) {
		List<CustField> result = new ArrayList<CustField>();
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustField.class);
		criteria.add(Restrictions.eq("target", target));
		criteria.addOrder(Order.asc("order"));

		result = criteria.list();

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void addCustField(CustField custField) {
		sessionFactory.getCurrentSession().save(custField);
		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteCustField(CustField custField) {
		sessionFactory.getCurrentSession().delete(custField);
		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateCustField(CustField custField) {
		sessionFactory.getCurrentSession().update(custField);
		
	}

	@Override
	public int getNewDisplayOrder(String target) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustField.class);
		criteria.add(Restrictions.eq("target", target));
		criteria.setProjection(Projections.rowCount()).uniqueResult();
		
		return ((Long) criteria.uniqueResult()).intValue(); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustField getCustField(int id) {
		List<CustField> result = new ArrayList<CustField>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from CustField where id= :id");
        query.setParameter("id", id);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else {
            return null;
        }
	}
	
	public int getCustFieldUsedCount(int id) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_CUST_FIELD_USED_COUNT).setParameter("id", id);
		return (Integer)query.uniqueResult();
	}
}
