/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.BaseDocument;

@Repository
public class DocumentDAOImpl implements DocumentDAO {

	@Autowired
    private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<BaseDocument> getDocuments() {
        Query querySM = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_ALL_DOCUMENT_QUERY).setResultTransformer(Transformers.aliasToBean(BaseDocument.class));
        
		return querySM.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<BaseDocument> searchByKeyword(String keyword) throws Exception {
        Query querySM = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_DOCUMENT_QUERY).setResultTransformer(Transformers.aliasToBean(BaseDocument.class));
        querySM.setParameter("keyword", "%" + keyword + "%");
        
		return querySM.list();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
