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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldDocument;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.ProductVersionDocument;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenDocument;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;


/**
 * This class represents for a Document Management Page
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="documentMgntBean")
@ViewScoped
public class DocumentMgntBean extends BaseBean {
	private static final long serialVersionUID = -7286580984729999995L;

	static Logger logger = Logger.getLogger(DocumentMgntBean.class);
	
	@ManagedProperty("#{fileStorageService}")
	private FileStorageService fileStorageService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;

	@ManagedProperty("#{versionService}")
    private VersionService versionService;

	private String id;
	private String type;
	private String caption;
	
	private Screen screen;
	private Field field;
	private ProductVersion productVersion;
	
	@PostConstruct
	public void init() {
		this.id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		this.type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
		
		if (ApplicationConstant.SCREEN_TARGET_KEY.equalsIgnoreCase(type)) {
			this.screen = this.screenService.getScreenOnly(Integer.valueOf(id)); 
			this.caption = String.format("Screen '%s'",this.screen.getName());
		}
		else if (ApplicationConstant.FIELD_TARGET_KEY.equalsIgnoreCase(type)) {
			this.field = this.fieldService.getFieldOnly(Integer.valueOf(id));
			this.caption = String.format("Field '%s'",this.field.getName());
		}
		else if (ApplicationConstant.PRODUCT_TARGET_KEY.equalsIgnoreCase(type)) {
			this.productVersion = this.versionService.getProductVersionOnly(Integer.valueOf(id));
			this.caption = String.format("Product '%s'", this.productVersion.getDisplayName());
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		if (ApplicationConstant.ERROR.equals(actionResult)) return;
		
		try {
			boolean isValidate = this.validate();
			this.actionResult = isValidate ? ApplicationConstant.SUCCESS : ApplicationConstant.ERROR;
	
			String fileNameSaved = this.fileStorageService.getFileName(this.type, this.id, event.getFile().getFileName(), event.getFile().getFileName());
			
			File fAttachedFile = new File(event.getFile().getFileName());
			List<String> result = this.fileStorageService.storeFile(fileNameSaved, event.getFile().getInputstream());
			
			if (ApplicationConstant.SCREEN_TARGET_KEY.equalsIgnoreCase(type)) {
				ScreenDocument sd = new ScreenDocument();
				sd.setTarget(this.screen);
				sd.setName(FilenameUtils.getName(fAttachedFile.getName()));
				sd.setFile(fileNameSaved);
				sd.setSize(result.get(1));
				
				this.screenService.addDocument(sd);
			}
			else if (ApplicationConstant.FIELD_TARGET_KEY.equalsIgnoreCase(type)) {
				FieldDocument sd = new FieldDocument();
				sd.setTarget(this.field);
				sd.setName(FilenameUtils.getName(fAttachedFile.getName()));
				sd.setFile(fileNameSaved);
				sd.setSize(result.get(1));
				
				this.fieldService.addDocument(sd);
			}
			else if (ApplicationConstant.PRODUCT_TARGET_KEY.equalsIgnoreCase(type)) {
				ProductVersionDocument sd = new ProductVersionDocument();
				sd.setTarget(this.productVersion);
				sd.setName(FilenameUtils.getName(fAttachedFile.getName()));
				sd.setFile(fileNameSaved);
				sd.setSize(result.get(1));
				
				this.versionService.addDocument(sd);
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The documents are uploaded successfully.", fAttachedFile.getName())));
			
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("There is an error when uploading files. Please contact System Adminstrator.")));
		}
	}
	
	private boolean validate() {
		if (ValidateUtils.isObjectEmpty(id) || ValidateUtils.isObjectEmpty(type)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("There is INVALID information carried along. Please contact System Adminstrator.")));
			
			return false; 
		}
		else if (!type.equalsIgnoreCase(ApplicationConstant.SCREEN_TARGET_KEY) &&
				!type.equalsIgnoreCase(ApplicationConstant.FIELD_TARGET_KEY) &&
				!type.equalsIgnoreCase(ApplicationConstant.PRODUCT_TARGET_KEY)) {
				
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("There is INVALID information carried along. Please contact System Adminstrator.")));
			
			return false;
		}
		
		return true;

	}
	
	public String getCaption() {
		return caption;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}