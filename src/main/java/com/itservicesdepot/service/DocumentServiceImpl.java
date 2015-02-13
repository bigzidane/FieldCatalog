/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.dao.DocumentDAO;
import com.itservicesdepot.model.BaseDocument;
import com.itservicesdepot.utils.ValidateUtils;

@Service("documentService")
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDAO documentDAO;
    
    public List<BaseDocument> searchByKeyword(String keyword) throws Exception {
    	if (ValidateUtils.isObjectEmpty(keyword)) {
    		return documentDAO.getDocuments();
    	}
    	else {
    		return documentDAO.searchByKeyword(keyword);
    	}
    }
}
