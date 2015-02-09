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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {
	static Logger logger = Logger.getLogger(FileStorageServiceImpl.class);
	
	public String storeFile(String baseFileName, InputStream in) {
		try {
			String basePath = System.getenv("OPENSHIFT_DATA_DIR");
			
			// development env only
			if (ValidateUtils.isObjectEmpty(basePath)) {
				basePath = "c:\\temp\\upload\\images\\";
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
			String now = formatter.format(DateTimeUtils.Now()); 
			String ext = FilenameUtils.getExtension(baseFileName);
			String fileName = baseFileName.replace("." + ext, now + "." + ext);

			OutputStream out = new FileOutputStream(new File(basePath + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			return fileName;
		} catch (Exception ex) {
			System.out.println("upload file error " + ex.getMessage());
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return "";
		}
	}
}