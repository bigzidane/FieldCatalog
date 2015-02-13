/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Service;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.utils.ValidateUtils;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {
	static Logger logger = Logger.getLogger(FileStorageServiceImpl.class);
	
	/**
	 * List[0]	:	file saved
	 * List[1]	:	file size
	 */
	public List<String> storeFile(String baseFileName, InputStream in) {
		try {
			String dataDir = FileStorageService.DATA_DIR;
			long size = 0;
			// development env only
			if (ValidateUtils.isObjectEmpty(dataDir)) dataDir = "c:\\temp\\";

			OutputStream out = new FileOutputStream(new File(dataDir + baseFileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
				size += read;
			}

			in.close();
			out.flush();
			out.close();

			List<String> result = new ArrayList<String>();
			result.add(baseFileName);
			result.add(String.valueOf(size));
			
			return result;
		} catch (Exception ex) {
			System.out.println("upload file error " + ex.getMessage());
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * To build a string for file stored on web server storage
	 * such as s_12_newdocument.png
	 */
	public String getFileName(String prefix, String key, String name, String originalfileName) {
		String fileNameSaved = prefix + ApplicationConstant.CUST_FIELD_SEPARATOR + key + ApplicationConstant.CUST_FIELD_SEPARATOR +
				FilenameUtils.getBaseName(name) + "." + FilenameUtils.getExtension(originalfileName);
		
		return fileNameSaved.toLowerCase().replaceAll(" ", "");
	}
	
	public boolean copyFile (String sourceFileName, String destFileName) {
		boolean result = true;

		String dataDir = FileStorageService.DATA_DIR;
		
		// development env only
		if (ValidateUtils.isObjectEmpty(dataDir)) dataDir = "c:\\temp\\";	
		
		File sourceFile = new  File(dataDir + sourceFileName);
		File destFile = new  File(dataDir + destFileName);
		
		try {
			FileUtils.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileName		:	this is a file name stored in data directory
	 * @param fileDisplay	:	this is a file name which is displayed on Download dialog
	 * @return
	 * @throws Exception
	 */
	public StreamedContent getStreamedContent(String fileName, String fileDisplay) throws Exception  {
		String dataDir = FileStorageService.DATA_DIR;
		
		// development env only
		if (ValidateUtils.isObjectEmpty(dataDir)) dataDir = "c:\\temp\\";
		
		File docFile = new File(dataDir, fileName);
		InputStream inputStream = FileUtils.openInputStream(docFile);
			
		StreamedContent file = new DefaultStreamedContent(inputStream,new MimetypesFileTypeMap().getContentType(docFile.getName()), fileDisplay);
	        
	    return file;
	}
}