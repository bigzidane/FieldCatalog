/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidateUtils {
	
	public static boolean isObjectEmpty(Object o) {
		if (o == null) return true;
		
		if (o instanceof ArrayList) {
			@SuppressWarnings("rawtypes")
			ArrayList array = (ArrayList)o;
			if (array.isEmpty()) return true;
		}
		else if (o instanceof String) {
			String string = (String)o;
			
			if (string.isEmpty()) return true;
		}
		else if (o instanceof HashMap) {
			@SuppressWarnings("rawtypes")
			HashMap hash = (HashMap)o;
			if (hash.isEmpty()) return true;
		}
		return false;
	}
	
	public static boolean isObjectNotEmpty(Object o) {
		return !isObjectEmpty(o);
	}
}