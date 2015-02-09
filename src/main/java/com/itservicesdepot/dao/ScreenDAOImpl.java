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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.Screen;

@Repository
public class ScreenDAOImpl implements ScreenDAO {
	static Logger logger = Logger.getLogger(ScreenDAOImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
      
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Screen> getScreens() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class);
        criteria.addOrder( Order.asc("name") );
        
        List<Screen> list = criteria.list();
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_FIELD_COUNT_QUERY).setResultTransformer(Transformers.aliasToBean(Screen.class));
    	
		List result = query.list();
		
		Iterator it = result.iterator();
		while ( it.hasNext() ) {
			Screen screen = (Screen)it.next();
			
			list.get(list.indexOf(screen)).setFieldsCount(screen.getFieldsCount());
		}
    	
    	return list;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Screen> getScreensByProductVersion(String productVersionId) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class,"s");
    	criteria.add(Restrictions.eq("s.productVersion.id", Integer.valueOf(productVersionId)));
        criteria.addOrder( Order.asc("name") );
        
        List<Screen> list = criteria.list();
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_FIELD_COUNT_PRODUCTVERSION_QUERY).setResultTransformer(Transformers.aliasToBean(Screen.class));
    	query.setParameter("productVersionId", productVersionId);
		List result = query.list();
		
		Iterator it = result.iterator();
		while ( it.hasNext() ) {
			Screen screen = (Screen)it.next();
			
			if (list.indexOf(screen) != -1) {
				list.get(list.indexOf(screen)).setFieldsCount(screen.getFieldsCount());
			}
		}
		
		return list;
    }
    
    @SuppressWarnings({ "unchecked" })
	public List<Screen> getDirectScreensByProductVersion(String productVersionId) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class,"s");
    	criteria.add(Restrictions.eq("s.productVersion.id", Integer.valueOf(productVersionId)));
    	criteria.add(Restrictions.isNull("parentId"));
        criteria.addOrder( Order.asc("name") );
        
        List<Screen> list = criteria.list();
        
		return list;
    }
    
    @SuppressWarnings("unchecked")
	public List<Screen> getChildScreens(int parentId) {
    	Query query = sessionFactory.getCurrentSession().createQuery("from Screen where parentId like :parentId");
    	query.setParameter("parentId", "%" + ApplicationConstant.COMMA_SEPARATOR + parentId + ApplicationConstant.COMMA_SEPARATOR + "%");
    	
    	return query.list();
    }
    
    @SuppressWarnings("unchecked")
	public Screen getScreen(int id) {
    	List<Screen> result = new ArrayList<Screen>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Screen where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Screen screen = (Screen)result.get(0);
        	
        	Hibernate.initialize(screen.getProductVersion());
        	Hibernate.initialize(screen.getScreenCustFields());
        	Hibernate.initialize(screen.getScreenTags());
        	Hibernate.initialize(screen.getScreenGroups());
        	Hibernate.initialize(screen.getScreenEvents());
        	Hibernate.initialize(screen.getScreenMessages());
        	
        	return screen;
        }
        else
            return null;
    }
    
    @SuppressWarnings("unchecked")
	public Screen getScreenOnly(int id) {
    	List<Screen> result = new ArrayList<Screen>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Screen where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Screen screen = (Screen)result.get(0);

        	return screen;
        }
        else
            return null;
    }
    
    @SuppressWarnings("unchecked")
	public Screen getScreen(String name) {
    	List<Screen> result = new ArrayList<Screen>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from Screen where name= :name");
        query.setParameter("name", name);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int addScreen(Screen screen) {
    	sessionFactory.getCurrentSession().save(screen);
    	return screen.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int updateScreen(Screen screen) {
    	sessionFactory.getCurrentSession().saveOrUpdate(screen);
    	
    	return screen.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int deleteScreen(Screen screen) {
    	sessionFactory.getCurrentSession().delete(screen);
    	
    	return screen.getId();
    }
    
    public List<Screen> searchByKeyword(String keyword) throws Exception {
    	Set<Screen> uniqueScreens = new HashSet<Screen>();
    	
    	FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

		fullTextSession.createIndexer(Screen.class).startAndWait();
		
		// search in main table
		QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Screen.class).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields("name", "description", "businessRule", "codeRule")
        													.matching(keyword).createQuery();

        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Screen.class);
        
        @SuppressWarnings("unchecked")
		List<Screen> screens = hibQuery.list();
        uniqueScreens.addAll(screens);
        
        // search in ScreenCustField
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class, "s");
        criteria.createAlias("s.screenCustFields", "sc");
        criteria.add(Restrictions.eqProperty("s.id", "sc.pk.screen.id"));
        criteria.add(Restrictions.like("sc.value", "%"+keyword+"%"));

		@SuppressWarnings("unchecked")
		List<Screen> screenCustFields = criteria.list();
		uniqueScreens.addAll(screenCustFields);
		
        // search in ScreenEvent
        criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class, "s");
        criteria.createAlias("s.screenEvents", "se");
        criteria.add(Restrictions.eqProperty("s.id", "se.pk.screen.id"));
        criteria.add(Restrictions.disjunction().add(Restrictions.like("se.businessRule", "%"+keyword+"%"))
        										.add(Restrictions.like("se.codeRule", "%"+keyword+"%")));
        
        @SuppressWarnings("unchecked")
		List<Screen> screenEvents = criteria.list();
        uniqueScreens.addAll(screenEvents);
        
        // search in ScreenTag
        criteria = sessionFactory.getCurrentSession().createCriteria(Screen.class, "s");
        criteria.createAlias("s.screenTags", "st");
        criteria.add(Restrictions.like("st.name", "%"+keyword+"%"));
        
        @SuppressWarnings("unchecked")
		List<Screen> screenTags = criteria.list();
        uniqueScreens.addAll(screenTags);
        
        // search in ScreenMessage
        Query querySM = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_SCREEN_MESSAGE_QUERY).setResultTransformer(Transformers.aliasToBean(Screen.class));
        querySM.setParameter("keyword", "%"+keyword + "%");
        @SuppressWarnings("unchecked")
		List<Screen> screenMessages = querySM.list();
        uniqueScreens.addAll(screenMessages);
        
        return new ArrayList<Screen>(uniqueScreens);
    }
    
    @SuppressWarnings("unchecked")
	public List<Screen> getScreenNameByIds(String ids) {
    	Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_SCREEN_NAME_QUERY).setResultTransformer(Transformers.aliasToBean(Screen.class));
    	query.setParameterList("ids", ids.split(ApplicationConstant.COMMA_SEPARATOR));
		return query.list();
    }
}
