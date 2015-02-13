/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.io.InputStream;
import java.util.List;

import org.primefaces.model.StreamedContent;


public interface FileStorageService {
	
	public static String DATA_DIR = System.getenv("OPENSHIFT_DATA_DIR");

	public List<String> storeFile(String baseFileName, InputStream in);
	
	public String getFileName(String prefix, String key, String name, String originalfileName);
	
	public boolean copyFile(String sourceFileName, String destFileName);

	public StreamedContent getStreamedContent(String fileName, String fileDisplay) throws Exception;
}