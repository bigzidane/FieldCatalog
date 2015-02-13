/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.ClonedObject;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldDocument;

public interface FieldDAO {

	public List<Field> getFields();
	
	public List<Field> getFieldsByScreenId(int id);
	
	public int getFieldsCountByScreenId(int id);

	public int addField(Field field);

	public int updateField(Field field);

	public int deleteField(Field field);

	public Field getField(int id);
	
	public Field getFieldOnly(int id);

	public Field getField(String id);
	
	public List<Field> searchByKeyword(String keyword) throws Exception;
	
	public List<ClonedObject> getClonedFieldMapping(int productVersionId);
	
	public void updateDependentIds(int id, String dependentIds);
	
	public FieldDocument getDocument(String name, int screenId);

	public int addDocument(FieldDocument document);
	
	public List<Field> getUniqueFields();
	
	public List<Field> getUniqueFieldsByScreenId(int screenId);
}
