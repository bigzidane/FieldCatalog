/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
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
    
    public List<BaseDocument> searchInContentByKeywork(String keyword) throws Exception {
    	List<BaseDocument> documents = new ArrayList<BaseDocument>();
    	
    	List<BaseDocument> allDocuments = this.documentDAO.getDocuments();
    	for (BaseDocument doc : allDocuments) {
    		if (ApplicationConstant.MICROSOFT_WORD_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(doc.getFile())) ||
    			ApplicationConstant.MICROSOFT_WORD_X_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(doc.getFile()))) {
    			
    			String dataDir = FileStorageServiceImpl.getDataDirectory();
    			
    			/* Create a FileInputStream object to read the input MS Word Document */
                FileInputStream inputDocument = new FileInputStream(new File(dataDir + doc.getFile())); 
                
                /* Create Word Extractor object */
                WordExtractor wordExactor=new WordExtractor(inputDocument);
                inputDocument.close();
                
                /* Create Scanner object */             
                Scanner documentScanner = new Scanner(wordExactor.getText());
                /* Define Search Pattern - we find for the word  "search" */
                Pattern words = Pattern.compile("(" +keyword + ")", Pattern.CASE_INSENSITIVE);
                
                /* Scan through every line */
                String key;
                int count = 0;
				while (documentScanner.hasNextLine() && count == 0) {
					/* search for the pattern in scanned line */
					key = documentScanner.findInLine(words);
					if (key != null) {
						count++;
					}
					/* Scan next line in document */
					documentScanner.nextLine();
				}
				
				wordExactor.close();
				documentScanner.close();
				
				if (count > 0) documents.add(doc);
    		}
    	}
    	return documents;
    }
}
