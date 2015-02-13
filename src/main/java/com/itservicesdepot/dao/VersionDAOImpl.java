/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.ProductVersionDocument;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenVersion;

@Repository
public class VersionDAOImpl implements VersionDAO {
   
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
	public List<ProductVersion> getUniqueVersions() {
    	Query query = sessionFactory.getCurrentSession().createSQLQuery("select distinct name from PRODUCT_VERSION");
    	query.setResultTransformer(new AliasToBeanResultTransformer(ProductVersion.class));
    	return query.list();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ProductVersion> getProductVersions() {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductVersion.class);

    	List<ProductVersion> list = criteria.list();
    	
    	Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_SCREEN_COUNT_ON_PRODUCT_VERSION_QUERY).
    								addScalar("screensCount",IntegerType.INSTANCE).
    								addScalar("id",IntegerType.INSTANCE).
    								setResultTransformer(Transformers.aliasToBean(ProductVersion.class));
    	
    	List result = query.list();
		Iterator it = result.iterator();
		while ( it.hasNext() ) {
			ProductVersion productVersion = (ProductVersion)it.next();
			
			list.get(list.indexOf(productVersion)).setScreensCount(productVersion.getScreensCount());
		}
        return list;
    }
    
    @SuppressWarnings("unchecked")
	public ProductVersion getProductVersionOnly(int id) {
    	List<ProductVersion> result = new ArrayList<ProductVersion>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from ProductVersion where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	ProductVersion productVersion = (ProductVersion)result.get(0);
        	
        	Hibernate.initialize(productVersion.getProduct().getProductCustFields());
        	Hibernate.initialize(productVersion.getProduct().getProductGroups());
        	Hibernate.initialize(productVersion.getProduct().getProductTags());
        	Hibernate.initialize(productVersion.getProductVersionDocuments());

        	return productVersion;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public ProductVersion getProductVersion(int id) {
    	List<ProductVersion> result = new ArrayList<ProductVersion>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from ProductVersion where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	ProductVersion productVersion = (ProductVersion)result.get(0);
        	
        	Hibernate.initialize(productVersion.getScreen());
        	return productVersion;
        }
        else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
	public ScreenVersion getScreenVersion(int id) {
    	List<ScreenVersion> result = new ArrayList<ScreenVersion>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from ScreenVersion where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	ScreenVersion screenVersion = (ScreenVersion)result.get(0);

        	Hibernate.initialize(screenVersion.getScreen());
        	return screenVersion;
        }
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<Screen> getScreensByProductVersionId(int productVersionId) {
    	Query query = sessionFactory.getCurrentSession().createQuery("from Screen where product_version_id = :id");
        query.setParameter("id", productVersionId);
        return query.list();
    }
    
	@SuppressWarnings("unchecked")
	public List<ProductVersion> getProductVersionsByProductId(int productId) {
    	Query query = sessionFactory.getCurrentSession().createQuery("from ProductVersion where product_id= :id");
        query.setParameter("id", productId);
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ScreenVersion> getScreenVersionsByScreenId(int screenId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from ScreenVersion where screen_id= :id");
        query.setParameter("id", screenId);
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public ProductVersion getProductVersion(String productName, String productVersionName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductVersion.class, "pv");
        criteria.createAlias("pv.product", "p");
        criteria.add(Restrictions.eqProperty("p.id", "pv.product.id"));
        criteria.add(Restrictions.eq("p.name", productName));
        criteria.add(Restrictions.eq("pv.name", productVersionName));
        
        List<ProductVersion> list = criteria.list();
        
        if (list.size() > 0) {
        	return list.get(0);
        }
        else {
        	return null;
        }
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public int addProductVersion(ProductVersion productVersion) {
		sessionFactory.getCurrentSession().save(productVersion);
		
		return productVersion.getId();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public int cloneProductVersion(ProductVersion productVersion, int actorId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.CLONE_PRODUCT_VERSION);
		query.setParameter("productVersionId", productVersion.getCloneFromVersionId());
		query.setParameter("productVersionName", productVersion.getName());
		query.setParameter("productVersionDescription", productVersion.getDescription());
		query.setParameter("actorId", actorId);
		
		Object cloneResult = (Object)query.uniqueResult();
		
		return Integer.valueOf(cloneResult.toString());
	}
	
    @SuppressWarnings("rawtypes")
	public ProductVersionDocument getDocument(String name, int screenId) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductVersionDocument.class);
    	criteria.add(Restrictions.eq("name", name));
    	criteria.add(Restrictions.eq("target.id", screenId));
    	
    	List result = criteria.list();
        if (result.size() > 0) {
        	ProductVersionDocument productVersionDocument = (ProductVersionDocument)result.get(0);

        	return productVersionDocument;
        }
        else {
            return null;
        }

    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int addDocument(ProductVersionDocument document) {
    	sessionFactory.getCurrentSession().save(document);
    	
    	return document.getId(); 
    }
}
