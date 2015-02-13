/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldDocument;

public interface FieldService {

	public List<Field> getFields();
	
	public List<Field> getFieldsByScreenId(int id);
	
	public int getFieldsCountByScreenId(int id);

	public int addField(Field field);

	public int updateField(Field field);

	public int deleteField(Field field);

	public Field getField(int id);
	
	public Field getFieldOnly(int id);

	public Field getField(String name);
	
	public List<Field> searchByKeyword(String keyword) throws Exception;
	
	public List<Field> getDependentIdFields(String dependentIds);
	
	public int addDocument(FieldDocument document);
	
	public List<Field> getUniqueFields();
	
	public List<Field> getUniqueFieldsByScreenId(int screenId);
}
