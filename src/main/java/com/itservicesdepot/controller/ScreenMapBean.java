/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Field List page. This page is allowed for all Roles.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="screenMapBean")  
@ViewScoped
public class ScreenMapBean extends BaseBean {
	private static final long serialVersionUID = -3058611652683064462L;

	static Logger logger = Logger.getLogger(ScreenMapBean.class);
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	private Screen screen;
	private MindmapNode screenRoot;
    private MindmapNode selectedNode;
    
    private List<SimpleEntry<String, String>> colorLegends;
	
	@PostConstruct
	public void init() {
		String screenId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(screenId)) {
			this.screen = this.screenService.getScreenOnly(Integer.valueOf(screenId));
			
			
			this.screenRoot = new DefaultMindmapNode(this.screen.getName(), String.format("Screen_%s", this.screen.getId()), ApplicationConstant.ROOT_NODE_COLOR, false);
			
			List<Field> fields = this.fieldService.getFieldsByScreenId(screen.getId());
			List<Screen> childScreens = this.screenService.getChildScreens(screen.getId());
			
			this.addScreens(this.screenRoot, childScreens);
			this.addFields(this.screenRoot, fields);
			
			this.colorLegends = buildColorLegends();
		}
	}

	public void onNodeSelect(SelectEvent event) {
        MindmapNode node = (MindmapNode) event.getObject();
         
        if(node.getChildren().isEmpty()) {
            String data = (String)node.getData();
 
            String[] keys = data.split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String id = keys[keys.length-1];
		    String type = keys[0];
		    
            if(ApplicationConstant.SCREEN_TARGET_ID.equals(type)) {
            	List<Field> fields = this.fieldService.getFieldsByScreenId(Integer.valueOf(id));
            	List<Screen> childScreens = this.screenService.getChildScreens(Integer.valueOf(id));
            	
            	this.addScreens(node, childScreens);
            	this.addFields(node, fields);
            }
            else if(ApplicationConstant.FIELD_TARGET_ID.equals(type)) {
            	// TODO: open a new browser for Field information
            } 
        }
	}
	
	private void addScreens(MindmapNode node, List<Screen> screens) {
		for (Screen screen : screens) {
			MindmapNode screenNode = new DefaultMindmapNode(screen.getName(), String.format("Screen_%s", screen.getId()), ApplicationConstant.SCREEN_NODE_COLOR, true);
			
			node.addNode(screenNode);
		}
	}
	
	private void addFields(MindmapNode node, List<Field> fields) {
		for (Field field : fields) {
			MindmapNode fieldNode = new DefaultMindmapNode(field.getName(), String.format("Field_%s", field.getId()), ApplicationConstant.FIELD_NODE_COLOR, true);
			
			node.addNode(fieldNode);
		}
	}
	
	public static List<SimpleEntry<String, String>> buildColorLegends() {
		List<SimpleEntry<String, String>> colorLegends = new ArrayList<SimpleEntry<String, String>>();
		
		SimpleEntry<String, String> root = new SimpleEntry<String, String>(ApplicationConstant.ROOT_NODE_COLOR, "Current Screen");
		SimpleEntry<String, String> childScreen = new SimpleEntry<String, String>(ApplicationConstant.SCREEN_NODE_COLOR, "Screen/Child Screens");
		SimpleEntry<String, String> fielScreen = new SimpleEntry<String, String>(ApplicationConstant.FIELD_NODE_COLOR, "Fields");
		
		colorLegends.add(root);
		colorLegends.add(childScreen);
		colorLegends.add(fielScreen);
		
		return colorLegends;
	}


	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public MindmapNode getScreenRoot() {
		return screenRoot;
	}

	public void setScreenRoot(MindmapNode screenRoot) {
		this.screenRoot = screenRoot;
	}

	public MindmapNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(MindmapNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public List<SimpleEntry<String, String>> getColorLegends() {
		return colorLegends;
	}

	public void setColorsLegends(List<SimpleEntry<String, String>> colorLegends) {
		this.colorLegends = colorLegends;
	}
}