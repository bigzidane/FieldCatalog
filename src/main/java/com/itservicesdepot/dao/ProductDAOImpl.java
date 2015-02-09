/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.model.Product;

@Repository
public class ProductDAOImpl implements ProductDAO {
   
    @Autowired
    private SessionFactory sessionFactory;
      
    @SuppressWarnings({ "unchecked" })
	public List<Product> getProducts() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
        
    	return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
	public Product getProduct(int id) {
    	List<Product> result = new ArrayList<Product>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Product where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Product product = (Product)result.get(0);
        	
        	Hibernate.initialize(product.getProductCustFields());
        	Hibernate.initialize(product.getProductTags());
        	Hibernate.initialize(product.getProductGroups());

        	return product;
        }
        else
            return null;
    }
    
    @SuppressWarnings("unchecked")
	public Product getProduct(String name) {
    	List<Product> result = new ArrayList<Product>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from Product where name= :name");
        query.setParameter("name", name);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int addProduct(Product product) {
    	sessionFactory.getCurrentSession().save(product);

    	return product.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int updateProduct(Product product) {
    	sessionFactory.getCurrentSession().saveOrUpdate(product);
    	
    	return product.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int deleteProduct(Product product) {
    	sessionFactory.getCurrentSession().delete(product);
    	
    	return product.getId();
    }
    
    public List<Product> searchByKeyword(String keyword) throws Exception {
    	Set<Product> uniqueProducts = new HashSet<Product>();
    	
    	FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

		fullTextSession.createIndexer(Product.class).startAndWait();
		
		// search in main table
		QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields("name", "description")
        													.matching(keyword).createQuery();

        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Product.class);
        
        @SuppressWarnings("unchecked")
		List<Product> products = hibQuery.list();
        uniqueProducts.addAll(products);
        
        // search in ProductCustField
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class, "p");
        criteria.createAlias("p.productCustFields", "pc");
        criteria.add(Restrictions.eqProperty("p.id", "pc.pk.product.id"));
        criteria.add(Restrictions.like("pc.value", "%"+keyword+"%"));

		@SuppressWarnings("unchecked")
		List<Product> productCustFields = criteria.list();
		uniqueProducts.addAll(productCustFields);
		
        // search in ProductTag
        criteria = sessionFactory.getCurrentSession().createCriteria(Product.class, "p");
        criteria.createAlias("p.productTags", "pt");
        criteria.add(Restrictions.like("pt.name", "%"+keyword+"%"));
        
        @SuppressWarnings("unchecked")
		List<Product> productTags = criteria.list();
        uniqueProducts.addAll(productTags);
        
        return new ArrayList<Product>(uniqueProducts);
    }
}
