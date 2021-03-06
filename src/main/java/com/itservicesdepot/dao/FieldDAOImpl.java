/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.DAOConstant;
import com.itservicesdepot.model.ClonedObject;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldDocument;
import com.itservicesdepot.utils.ValidateUtils;

@Repository
public class FieldDAOImpl implements FieldDAO {
   
    @Autowired
    private SessionFactory sessionFactory;
      
    @SuppressWarnings("unchecked")
	public List<Field> getFields() {
        Criteria result = sessionFactory.getCurrentSession().createCriteria(Field.class);
        
        return (List<Field>)result.list();
    }
    
    @SuppressWarnings("unchecked")
	public Field getField(int id) {
    	List<Field> result = new ArrayList<Field>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Field where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Field field = (Field)result.get(0);
        	Hibernate.initialize(field.getScreen());
        	Hibernate.initialize(field.getFieldCustFields());
        	Hibernate.initialize(field.getFieldTags());
        	Hibernate.initialize(field.getFieldGroups());
        	Hibernate.initialize(field.getFieldEvents());
        	Hibernate.initialize(field.getFieldMessages());
        	Hibernate.initialize(field.getFieldDocuments());
        	
        	return field;
        }
        else
            return null;
    }
    
    @SuppressWarnings("unchecked")
	public Field getFieldOnly(int id) {
    	List<Field> result = new ArrayList<Field>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Field where id=?").setParameter(0, id);
    	
    	result = query.list();
        if (result.size() > 0) {
        	Field field = (Field)result.get(0);
        	Hibernate.initialize(field.getScreen());
        	
        	return field;
        }
        else
            return null;
    }
    
    @SuppressWarnings("unchecked")
	public List<Field> getFieldsByScreenId(int id) {
    	
    	List<Field> result = new ArrayList<Field>();
    	Query query = sessionFactory.getCurrentSession().createQuery("from Field where screen_id=?").setParameter(0, id);
    	
    	result = query.list();
    	
    	return result;
    }
    
    public int getFieldsCountByScreenId(int id) {
    	
    	Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(1) from Field where screen_id=:screen_id");
    	query.setString("screen_id", String.valueOf(id));
    	BigInteger count = (BigInteger)query.uniqueResult();
    	
    	return count.intValue();
    }
    
    @SuppressWarnings("unchecked")
	public Field getField(String name) {
    	List<Field> result = new ArrayList<Field>();
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from Field where name= :name");
        query.setParameter("name", name);
        result = query.list();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int addField(Field field) {
    	sessionFactory.getCurrentSession().save(field);
    	
    	return field.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int updateField(Field field) {
    	sessionFactory.getCurrentSession().saveOrUpdate(field);
    	
    	return field.getId();
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int deleteField(Field field) {
    	sessionFactory.getCurrentSession().delete(field);
    	
    	return field.getId();
    }
    
    public List<Field> searchByKeyword(String keyword) throws Exception {
    	Set<Field> uniqueFields = new HashSet<Field>();
    	
    	FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

		fullTextSession.createIndexer(Field.class).startAndWait();
		
		QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Field.class).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields("name", "description", "businessRule", "codeRule")
        													.matching(keyword).createQuery();

        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Field.class);
        
        @SuppressWarnings("unchecked")
		List<Field> fields = hibQuery.list();
        uniqueFields.addAll(fields);
        
        // search in FieldCustField
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Field.class, "s");
        criteria.createAlias("s.fieldCustFields", "sc");
        criteria.add(Restrictions.eqProperty("s.id", "sc.pk.field.id"));
        criteria.add(Restrictions.like("sc.value", "%"+keyword+"%"));

		@SuppressWarnings("unchecked")
		List<Field> fieldCustFields = criteria.list();
		uniqueFields.addAll(fieldCustFields);
		
        // search in FieldEvent
        criteria = sessionFactory.getCurrentSession().createCriteria(Field.class, "s");
        criteria.createAlias("s.fieldEvents", "se");
        criteria.add(Restrictions.eqProperty("s.id", "se.pk.field.id"));
        criteria.add(Restrictions.disjunction().add(Restrictions.like("se.businessRule", "%"+keyword+"%"))
        										.add(Restrictions.like("se.codeRule", "%"+keyword+"%")));
        
        @SuppressWarnings("unchecked")
		List<Field> fieldEvents = criteria.list();
        uniqueFields.addAll(fieldEvents);
        
        // search in FieldTag
        criteria = sessionFactory.getCurrentSession().createCriteria(Field.class, "s");
        criteria.createAlias("s.fieldTags", "st");
        criteria.add(Restrictions.like("st.name", "%"+keyword+"%"));
        
        @SuppressWarnings("unchecked")
		List<Field> fieldTags = criteria.list();
        uniqueFields.addAll(fieldTags);
        
        // search in FieldMessage
        Query querySM = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_FIELD_MESSAGE_QUERY).setResultTransformer(Transformers.aliasToBean(Field.class));
        querySM.setParameter("keyword", keyword);
        @SuppressWarnings("unchecked")
		List<Field> fieldMessages = querySM.list();
        uniqueFields.addAll(fieldMessages);
        
        return new ArrayList<Field>(uniqueFields);
    }
    
    @SuppressWarnings("rawtypes")
	public List<ClonedObject> getClonedFieldMapping(int productVersionId) {
    	List<ClonedObject> clonedFields = new ArrayList<ClonedObject>();
    	
    	Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.GET_CLONED_FIELD_MAPPING_QUERY);
    	query.setParameter("id", productVersionId);
    	
    	List result = query.list();
    	
    	Iterator it = result.iterator();
		while ( it.hasNext() ) {
			Object[] objects = (Object[])it.next();
			
			ClonedObject cs = new ClonedObject();
			cs.setSourceId(Integer.valueOf(objects[0].toString()));
			cs.setTaretId(Integer.valueOf(objects[1].toString()));
			
			if (ValidateUtils.isObjectNotEmpty(objects[2])) {
				cs.setRelatedIds(objects[2].toString());
			}
			else {
				cs.setRelatedIds(null);
			}
			
			clonedFields.add(cs);
		}
    	
		return clonedFields;
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateDependentIds(int id, String dependentIds) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(DAOConstant.UPDATE_FIELD_DEPENDENT_IDS_QUERY);
		query.setParameter("id", id);
		query.setParameter("dependentIds", dependentIds);
		
		query.executeUpdate();
	}
    
    @SuppressWarnings("rawtypes")
	public FieldDocument getDocument(String name, int screenId) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FieldDocument.class);
    	criteria.add(Restrictions.eq("name", name));
    	criteria.add(Restrictions.eq("target.id", screenId));
    	
    	List result = criteria.list();
        if (result.size() > 0) {
        	FieldDocument fieldDocument = (FieldDocument)result.get(0);

        	return fieldDocument;
        }
        else {
            return null;
        }

    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public int addDocument(FieldDocument document) {
    	sessionFactory.getCurrentSession().save(document);
    	
    	return document.getId(); 
    }
    
    @SuppressWarnings("unchecked")
   	public List<Field> getUniqueFields() {
       	Query query = sessionFactory.getCurrentSession().createSQLQuery("select distinct name from FIELD");
       	query.setResultTransformer(new AliasToBeanResultTransformer(Field.class));
       	return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Field> getUniqueFieldsByScreenId(int screenId) {
    	Query query = sessionFactory.getCurrentSession().createSQLQuery("select distinct name from FIELD where screen_Id=:id");
    	query.setParameter("id", screenId);
       	query.setResultTransformer(new AliasToBeanResultTransformer(Field.class));
       	return query.list();
    }
}
