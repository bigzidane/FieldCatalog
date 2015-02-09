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
											"where t.ID = pt.TAG_ID group by pt.product_id \n" +
											"union all \n" +
											"select t.name as name, count(st.screen_id) as tagCount from tag t, screen_tag st \n" +
											"where t.ID = st.TAG_ID group by st.screen_id \n" +
											"union all \n" +
											"select t.name as name, count(ft.field_id) as tagCount from tag t, field_tag ft \n" +
											"where t.ID = ft.TAG_ID group by ft.field_id \n" +
											") t group by t.name";			
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
	
	// get Cust Fields Used count
	public static String GET_CUST_FIELD_USED_COUNT = "call checkIfCustFieldUsed(:id)";
	public static String GET_EVENT_USED_COUNT = "call checkIfEventUsed(:id)";
	public static String GET_MESSAGE_USED_COUNT = "call checkIfMessageUsed(:id)";
	public static String GET_GROUP_USED_COUNT = "call checkIfGroupUsed(:id)";
	public static String GET_USER_USED_COUNT = "call checkIfUserUsed(:id)";
	
	// clone Product Version
	public static String CLONE_PRODUCT_VERSION = "call cloneProductVersion(:productVersionId,:productVersionName,:productVersionDescription,:actorId)";
}