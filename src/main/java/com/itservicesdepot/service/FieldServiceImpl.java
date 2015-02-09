/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.dao.FieldDAO;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.utils.ValidateUtils;

@Service("fieldService")
@Transactional(readOnly = true)
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldDAO fieldDAO;
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int addField(Field field) {
        return fieldDAO.addField(field);
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int updateField(Field field) {
        return fieldDAO.updateField(field);
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int deleteField(Field field) {
        return fieldDAO.deleteField(field);
    }

    public List<Field> getDependentIdFields(String dependentIds) {
    	List<Field> fields = new ArrayList<Field>();
    	
    	if (ValidateUtils.isObjectEmpty(dependentIds)) {
    		return fields;
    	}
    	
    	String[] ids = dependentIds.split(ApplicationConstant.COMMA_SEPARATOR);
    	for (String id : ids) {
    		if (ValidateUtils.isObjectEmpty(id)) continue;
    		Field field = new Field();
    		field.setId(Integer.valueOf(id));
    		
    		fields.add(field);
    	}
    	
    	return fields;
    }
    
	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	public List<Field> getFields() {
    	return fieldDAO.getFields();
    }
    
    public Field getField(int id) {
        return fieldDAO.getField(id);
    }
    
    public Field getFieldOnly(int id) {
    	return fieldDAO.getFieldOnly(id);
    }
    
    public Field getField(String name) {
        Field field = fieldDAO.getField(name);
        return field;
    }
    
    public List<Field> getFieldsByScreenId(int id) {
    	return fieldDAO.getFieldsByScreenId(id);
    }
    
    public int getFieldsCountByScreenId(int id) {
    	return fieldDAO.getFieldsCountByScreenId(id);
    }
    
    public List<Field> searchByKeyword(String keyword) throws Exception {
    	if (ValidateUtils.isObjectEmpty(keyword)) {
    		return fieldDAO.getFields();
    	}
    	else {
    		return fieldDAO.searchByKeyword(keyword);
    	}
    }
}
