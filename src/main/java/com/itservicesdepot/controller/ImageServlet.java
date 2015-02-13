/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.utils.ValidateUtils;

public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = -8476741726923861990L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String filename = request.getPathInfo().substring(1);
		String dataDir = FileStorageService.DATA_DIR;
		
		// development env only
		if (ValidateUtils.isObjectEmpty(dataDir)) dataDir = "c:\\temp\\";
		
        File file = new File(dataDir, filename);
        
        response.setHeader("Content-Type", new MimetypesFileTypeMap().getContentType(file));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
	}
}