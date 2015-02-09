/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.ScreenDAO;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.utils.ValidateUtils;

@Service("screenService")
@Transactional(readOnly = true)
public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenDAO screenDAO;
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public Result addScreen(Screen screen) {
    	Result result = new Result();
    	
    	Screen existingScreen = this.screenDAO.getScreen(screen.getName());
    	if (ValidateUtils.isObjectNotEmpty(existingScreen)) {
    		if (screen.getProductVersion().getProduct().getId() == existingScreen.getProductVersion().getProduct().getId()) {
    			result.setCode(ErrorCodeConstant.DUPLICATE_ENTRY);
    			return result;
    		}
    	}
    	
        result.setId(screenDAO.addScreen(screen));
        
        return result;
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int updateScreen(Screen screen) {
        return screenDAO.updateScreen(screen);
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int deleteScreen(Screen screen) {
        return screenDAO.deleteScreen(screen);
    }
    
	public ScreenDAO getScreenDAO() {
		return screenDAO;
	}

	public void setScreenDAO(ScreenDAO screenDAO) {
		this.screenDAO = screenDAO;
	}

	public List<Screen> getScreens() {
    	return screenDAO.getScreens();
    }
    
	public List<Screen> getScreensByProductVersion(String productVersionId) {
		return screenDAO.getScreensByProductVersion(productVersionId);
	}
	
	public List<Screen> getDirectScreensByProductVersion(String productVersionId) {
			return screenDAO.getDirectScreensByProductVersion(productVersionId);
	}
	
	public List<Screen> getChildScreens(int parentId) {
		return screenDAO.getChildScreens(parentId);
	}
	
    public Screen getScreen(int id) {
        return screenDAO.getScreen(id);
    }
    
    public Screen getScreenOnly(int id) {
    	return screenDAO.getScreenOnly(id);
    }
    
    public Screen getScreen(String name) {
        return screenDAO.getScreen(name);
    }
    
    public List<Screen> searchByKeyword(String keyword) throws Exception {
    	if (ValidateUtils.isObjectEmpty(keyword)) {
    		return screenDAO.getScreens();
    	}
    	else {
    		return screenDAO.searchByKeyword(keyword);
    	}
    }
    
    public List<Screen> getParentIdScreens(String parentIds) {
    	List<Screen> screens = new ArrayList<Screen>();
    	
    	if (ValidateUtils.isObjectEmpty(parentIds)) {
    		return screens;
    	}
    	
    	String[] ids = parentIds.split(ApplicationConstant.COMMA_SEPARATOR);
    	for (String id : ids) {
    		if (ValidateUtils.isObjectEmpty(id)) continue;
    		Screen screen = new Screen();
    		screen.setId(Integer.valueOf(id));
    		
    		screens.add(screen);
    	}
    	
    	return screens;
    }
    
    public List<Screen> getParentScreens(String parentIds) {
    	List<Screen> screens = new ArrayList<Screen>();
    	
    	if (ValidateUtils.isObjectEmpty(parentIds)) {
    		return screens;
    	}
    	
    	parentIds = StringUtils.removeStart(parentIds, ApplicationConstant.COMMA_SEPARATOR);
    	parentIds = StringUtils.removeEnd(parentIds, ApplicationConstant.COMMA_SEPARATOR);

    	return this.screenDAO.getScreenNameByIds(parentIds);
    }
}
