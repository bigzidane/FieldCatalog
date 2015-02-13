/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.FieldDAO;
import com.itservicesdepot.dao.ScreenDAO;
import com.itservicesdepot.dao.VersionDAO;
import com.itservicesdepot.model.ClonedObject;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.ProductVersionDocument;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenVersion;
import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.ValidateUtils;

@Service("versionService")
@Transactional(readOnly = true)
public class VersionServiceImpl implements VersionService {
	
	@Autowired
    private VersionDAO versionDAO;
	
	@Autowired
    private ScreenDAO screenDAO;
	
	@Autowired
    private FieldDAO fieldDAO;
	
	@Autowired
	private FileStorageService fileStoreageService;

	public List<ProductVersion> getUniqueVersions() {
		return versionDAO.getUniqueVersions();
	}
	
	public ProductVersion getProductVersionOnly(int id) {
		return versionDAO.getProductVersionOnly(id);
	}
	
	public ProductVersion getProductVersion(int id) {
		return versionDAO.getProductVersion(id);
	}
	
	public ScreenVersion getScreenVersion(int id) {
		return versionDAO.getScreenVersion(id);
	}
	
	public List<ProductVersion> getProductVersionsByProductId(int productId) {
		return versionDAO.getProductVersionsByProductId(productId);
	}
	
	public List<ScreenVersion> getScreenVersionsByScreenId(int screenId)  {
		return versionDAO.getScreenVersionsByScreenId(screenId);
	}
	
	public List<Screen> getScreensByProductVersionId(int productVersionId) {
		return versionDAO.getScreensByProductVersionId(productVersionId);
	}
	
	public ProductVersion getProductVersion(String productName, String productVersionName) {
		return versionDAO.getProductVersion(productName, productVersionName);
	}
	
	public VersionDAO getVersionDAO() {
		return versionDAO;
	}

	public void setVersionDAO(VersionDAO versionDAO) {
		this.versionDAO = versionDAO;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
	public Result addProductVersion(ProductVersion productVersion, int actorId) throws Exception {
		Result result = new Result();
		
		List<ProductVersion> productVersions = this.versionDAO.getProductVersionsByProductId(productVersion.getProduct().getId());
        for (ProductVersion existingVersion : productVersions) {
        	if (productVersion.getName().equalsIgnoreCase(existingVersion.getName())) {
        		result.setCode(ErrorCodeConstant.DUPLICATE_ENTRY);
        		return result;
        	}
        	else {
        		int vc = ApplicationUtils.versionCompare(productVersion.getName(), existingVersion.getName());
        		if (vc <=0 ) {
        			result.setCode(ErrorCodeConstant.ERROR_ADD_ENTRY_NAME_VIOLATION);
        			return result;
        		}
        	}
        }
        
		if (productVersion.getCloneFromVersionId() == 0) {
			result.setId(this.versionDAO.addProductVersion(productVersion));
		}
		else {
			int productVersionId = this.versionDAO.cloneProductVersion(productVersion, actorId);
			
			// clone images
			List<Screen> screens = versionDAO.getScreensByProductVersionId(productVersion.getCloneFromVersionId());
			for (Screen screen : screens) {
				if (ValidateUtils.isObjectNotEmpty(screen.getAttachedFile())) {
					String destFileName = this.fileStoreageService.getFileName(ApplicationConstant.SCREEN_TARGET_KEY, String.valueOf(productVersionId), screen.getName(), screen.getAttachedFile());
					this.fileStoreageService.copyFile(screen.getAttachedFile(), destFileName);
				}
			}
			
			// update Screen parent relationship 
			List<ClonedObject> cloneScreens = this.screenDAO.getClonedScreenMapping(productVersionId);
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (ClonedObject clonedScreen : cloneScreens) {
				map.put(clonedScreen.getSourceId(), clonedScreen.getTaretId());
			}
			
			for (ClonedObject clonedScreen : cloneScreens) {
				if (ValidateUtils.isObjectNotEmpty(clonedScreen.getRelatedIds())) {
					String newParentIds = ApplicationConstant.COMMA_SEPARATOR;
					
					String[] ids = ApplicationUtils.split(clonedScreen.getRelatedIds(), ApplicationConstant.COMMA_SEPARATOR);
					for (String id : ids) {
						if (ValidateUtils.isObjectEmpty(id)) continue;
						
						Integer newId = map.get(Integer.valueOf(id));
						newParentIds += String.valueOf(newId) + ApplicationConstant.COMMA_SEPARATOR;
					}
					
					clonedScreen.setRelatedIds(newParentIds);
				}
			}
			
			for (ClonedObject clonedScreen : cloneScreens) {
				if (ValidateUtils.isObjectNotEmpty(clonedScreen.getRelatedIds())) {
					this.screenDAO.updateParentIds(clonedScreen.getTaretId(), clonedScreen.getRelatedIds());
				}
			}
			
			// update Fields dependent relationship
			List<ClonedObject> clonedFields = this.fieldDAO.getClonedFieldMapping(productVersionId);
			HashMap<Integer, Integer> mapFields = new HashMap<Integer, Integer>();
			for (ClonedObject clonedField : clonedFields) {
				mapFields.put(clonedField.getSourceId(), clonedField.getTaretId());
			}
			
			for (ClonedObject clonedField : clonedFields) {
				if (ValidateUtils.isObjectNotEmpty(clonedField.getRelatedIds())) {
					String newDependentIds = ApplicationConstant.COMMA_SEPARATOR;
					
					String[] ids = ApplicationUtils.split(clonedField.getRelatedIds(), ApplicationConstant.COMMA_SEPARATOR);
					for (String id : ids) {
						if (ValidateUtils.isObjectEmpty(id)) continue;
						
						Integer newId = mapFields.get(Integer.valueOf(id));
						newDependentIds += String.valueOf(newId) + ApplicationConstant.COMMA_SEPARATOR;
					}
					
					clonedField.setRelatedIds(newDependentIds);
				}
			}
			
			for (ClonedObject clonedField : clonedFields) {
				if (ValidateUtils.isObjectNotEmpty(clonedField.getRelatedIds())) {
					this.fieldDAO.updateDependentIds(clonedField.getTaretId(), clonedField.getRelatedIds());
				}
			}
			
			// return to bean new product version id
			result.setId(productVersionId);
		}
		
		return result;
		
	}

	@Override
	public List<ProductVersion> getProductVersions() {
		return this.versionDAO.getProductVersions();
	}

	public FileStorageService getFileStoreageService() {
		return fileStoreageService;
	}

	public void setFileStoreageService(FileStorageService fileStoreageService) {
		this.fileStoreageService = fileStoreageService;
	}

	public ScreenDAO getScreenDAO() {
		return screenDAO;
	}

	public void setScreenDAO(ScreenDAO screenDAO) {
		this.screenDAO = screenDAO;
	}

	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int addDocument(ProductVersionDocument document) {
    	ProductVersionDocument productDocument = this.versionDAO.getDocument(document.getName(), document.getTarget().getId());
    	if (ValidateUtils.isObjectEmpty(productDocument)) {
    		return this.versionDAO.addDocument(document);
    	}
    	return 0;
    }
}