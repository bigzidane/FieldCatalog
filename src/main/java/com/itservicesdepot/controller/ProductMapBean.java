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
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Field List page. This page is allowed for all Roles.
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="productMapBean")  
@ViewScoped
public class ProductMapBean extends BaseBean {
	private static final long serialVersionUID = -4820650544024232831L;

	static Logger logger = Logger.getLogger(ProductMapBean.class);
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	private ProductVersion productVersion;	// this is a list of all productVersions available in entire system.
	private MindmapNode productVersionRoot;
    private MindmapNode selectedNode;
    
    private List<SimpleEntry<String, String>> colorLegends;
	
	@PostConstruct
	public void init() {
		String productVersionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(productVersionId)) {
			this.productVersion = this.versionService.getProductVersionOnly(Integer.valueOf(productVersionId));
			
			this.productVersionRoot = new DefaultMindmapNode(this.productVersion.getDisplayName(), String.format("Product_%s", this.productVersion.getId()), ApplicationConstant.ROOT_NODE_COLOR, false);
			
			List<Screen> directScreens = this.screenService.getDirectScreensByProductVersion(productVersionId);
			this.addScreens(this.productVersionRoot, directScreens);
			
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
			MindmapNode screenNode  = new DefaultMindmapNode(screen.getName(), String.format("Screen_%s", screen.getId()), ApplicationConstant.SCREEN_NODE_COLOR, true);
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
		
		SimpleEntry<String, String> root = new SimpleEntry<String, String>(ApplicationConstant.ROOT_NODE_COLOR, "Current Product");
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

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MindmapNode getProductVersionRoot() {
		return productVersionRoot;
	}

	public void setProductVersionRoot(MindmapNode productVersionRoot) {
		this.productVersionRoot = productVersionRoot;
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

	public void setColorLegends(List<SimpleEntry<String, String>> colorLegends) {
		this.colorLegends = colorLegends;
	}	
}