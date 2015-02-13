/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.constant;

/**
 * This class consists all constants used across application (all layers)
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

public class DAOConstant {
	public static String GET_TAG_CLOUD_QUERY = "select t.name, sum(t.tagCount) from ( \n" +
											"select t.name as name, count(pt.product_id) as tagCount from tag t, product_tag pt \n" +
											"where t.ID = pt.TAG_ID group by t.name \n" +
											"union all \n" +
											"select t.name as name, count(st.screen_id) as tagCount from tag t, screen_tag st \n" +
											"where t.ID = st.TAG_ID group by t.name \n" +
											"union all \n" +
											"select t.name as name, count(ft.field_id) as tagCount from tag t, field_tag ft \n" +
											"where t.ID = ft.TAG_ID group by t.name \n" +
											") t group by t.name";			
	
	public static String GET_ALL_DOCUMENT_QUERY = "select sd.id as id, sd.name as name, sd.file as file, concat('Screen: ' , s.NAME)  as description from screen_document sd, screen s \n" +
												"where sd.SCREEN_ID = s.id \n" +
												"union all \n" +
												"select fd.id, fd.name , fd.file as file, concat('Field: ' , f.NAME)  as description from field_document fd, field f \n" +
												"where fd.FIELD_ID = f.id \n" +
												"union all \n" +
												"select pvd.id, pvd.name , pvd.file as file, concat('Prouct Version: ' , pv.NAME)  as description from product_version_document pvd, product_version pv \n" +
												"where pvd.PRODUCT_VERSION_ID = pv.ID ";
	
	public static String GET_DOCUMENT_QUERY = "select sd.id as id, sd.name as name, sd.file as file, concat('Screen: ' , s.NAME)  as description from screen_document sd, screen s \n" +
												"where sd.SCREEN_ID = s.id and sd.name like :keyword  \n" +
												"union all \n" +
												"select fd.id, fd.name , fd.file as file, concat('Field: ' , f.NAME)  as description from field_document fd, field f \n" +
												"where fd.FIELD_ID = f.id and fd.name like :keyword \n" +
												"union all \n" +
												"select pvd.id, pvd.name , pvd.file as file, concat('Product Version: ' , pv.NAME)  as description from product_version_document pvd, product_version pv \n" +
												"where pvd.PRODUCT_VERSION_ID = pv.ID and pvd.name like :keyword";
	
	// get screens by message
	public static String GET_SCREEN_MESSAGE_QUERY = "select s.id as id, s.name as name from screen s, screen_message sm, system_message m where s.ID = sm.SCREEN_ID and m.ID = sm.MESSAGE_ID and m.CODE like :keyword";
	
	// get fields by message
	public static String GET_FIELD_MESSAGE_QUERY = "select f.id as id, f.name as name from field f, field_message fm, system_message m where f.ID = fm.FIELD_ID and m.ID = fm.MESSAGE_ID and m.CODE like :keyword";
	
	// get count of screens
	public static String GET_SCREEN_COUNT_ON_PRODUCT_VERSION_QUERY = "select pv.id as id, count(s.id) as screensCount from product_version pv, screen s where pv.id = s.product_version_id group by pv.id";
	
	// get count of fields
	public static String GET_FIELD_COUNT_QUERY = "select s.id, cast(count(f.id) as char(10)) as fieldsCount from screen s left outer join field f on s.ID = f.SCREEN_ID group by s.id";
	public static String GET_FIELD_COUNT_PRODUCTVERSION_QUERY = "select s.id, cast(count(f.id) as char(10)) as fieldsCount from screen s left outer join field f on s.ID = f.SCREEN_ID and s.product_version_id = :productVersionId group by s.id";

	// get screen name by id
	public static String GET_SCREEN_NAME_QUERY = "select s.id as id, s.name as name, s.parent_id as parentId from screen s where id in (:ids)";
	
	public static String GET_CLONED_SCREEN_MAPPING_QUERY = "select s1.id as sid, s2.id as tid, s1.parent_id from screen s1, screen s2 where s2.product_version_id = :id and s1.id = s2.ref_id";

	public static String GET_CLONED_FIELD_MAPPING_QUERY = "select f1.id as sid, f2.id as tid, f1.dependent_id from field f1, field f2 where f1.id = f2.ref_id and f2.screen_id in (select id from screen where product_version_id=:id)";
	
	// get Cust Fields Used count
	public static String GET_CUST_FIELD_USED_COUNT = "call checkIfCustFieldUsed(:id)";
	public static String GET_EVENT_USED_COUNT = "call checkIfEventUsed(:id)";
	public static String GET_MESSAGE_USED_COUNT = "call checkIfMessageUsed(:id)";
	public static String GET_GROUP_USED_COUNT = "call checkIfGroupUsed(:id)";
	public static String GET_USER_USED_COUNT = "call checkIfUserUsed(:id)";
	
	// clone Product Version
	public static String CLONE_PRODUCT_VERSION = "call cloneProductVersion(:productVersionId,:productVersionName,:productVersionDescription,:actorId)";
	
	public static String UPDATE_SCREEN_PARENT_IDS_QUERY = "update screen set parent_id=:parentIds where id=:id";
	public static String UPDATE_FIELD_DEPENDENT_IDS_QUERY = "update field set dependent_id=:dependentIds where id=:id";
}