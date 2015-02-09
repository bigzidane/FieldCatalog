/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.springframework.security.access.prepost.PreAuthorize;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.service.CustFieldService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class to handle 2 pages
 * 	1.	Manage Custom Fields
 * 	2.	Add a new Cust Field
 * 
 * Only Admin/Designer can perform Add/Edit/Delete custom fields
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="custFieldMgntBean")
@ViewScoped
public class CustFieldMgntBean extends BaseBean {
	private static final long serialVersionUID = 8198461069069899469L;

	static Logger logger = Logger.getLogger(CustFieldMgntBean.class);
	
	@ManagedProperty("#{custFieldService}")
    private CustFieldService custFieldService;
	
	// for Manage Cust Fields page
	private String target;
	private List<SelectItem> targets;
	private List<CustField> custFields = new ArrayList<CustField>();
	private List<String> custFieldDisplayOrders = new ArrayList<String>();
	
	// for Add Cust Field page
	private CustField custField = new CustField();
	private List<SelectItem> custFieldTypes;
	
	@PostConstruct
	public void init() {
		targets = new ArrayList<SelectItem>();
		targets.add(new SelectItem(ApplicationConstant.PRODUCT_TARGET_ID, ApplicationConstant.PRODUCT_TARGET_ID));
		targets.add(new SelectItem(ApplicationConstant.SCREEN_TARGET_ID, ApplicationConstant.SCREEN_TARGET_ID));
		targets.add(new SelectItem(ApplicationConstant.FIELD_TARGET_ID, ApplicationConstant.FIELD_TARGET_ID));
		
		custFieldTypes = new ArrayList<SelectItem>();
		custFieldTypes.add(new SelectItem(ApplicationConstant.INPUT_TEXT, ApplicationConstant.INPUT_TEXT));
		custFieldTypes.add(new SelectItem(ApplicationConstant.INPUT_TEXT_AREA, ApplicationConstant.INPUT_TEXT_AREA));
		custFieldTypes.add(new SelectItem(ApplicationConstant.CHECK_BOX, ApplicationConstant.CHECK_BOX));
		custFieldTypes.add(new SelectItem(ApplicationConstant.RADIO, ApplicationConstant.RADIO));
		custFieldTypes.add(new SelectItem(ApplicationConstant.SELECT, ApplicationConstant.SELECT));
		custFieldTypes.add(new SelectItem(ApplicationConstant.EDITOR, ApplicationConstant.EDITOR));
	}
	
	public String onFlowProcess(FlowEvent event) {
		if ("Target".equalsIgnoreCase(event.getOldStep())) {
			this.custFields = this.getCustFieldsByTarget(this.target);
			buildCustFieldDisplayOrder(this.custFields);
		}
		return event.getNewStep();
    }
	
	private List<CustField> getCustFieldsByTarget(String selectedTarget) {
		return custFieldService.getCustFieldsByTarget(selectedTarget);
	}
	
	private void buildCustFieldDisplayOrder(List<CustField> custFields) {
		for (int i=1; i<= custFields.size(); i++) {
			this.custFieldDisplayOrders.add(String.valueOf(i));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addCustField() {
		try {
			
			this.buildCustFieldModel(this.custField);
			
        	this.getCustFieldService().addCustField(this.custField);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Custom Field is added successfully", custField.getLabel())));
        	
        	this.reset();
        	
        	return ApplicationConstant.SUCCESS;
        	
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Custom Field is NOT added successfully. Please contact System Administrator.", custField.getLabel())));
        	
        	logger.error(e.getMessage(), e);
        
        	return ApplicationConstant.ERROR;
        }    
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateCustField(CustField custField) {
        try {
        	this.getCustFieldService().updateCustField(custField);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Custom Field is update successfully", custField.getLabel())));
        	
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Custom Field is NOT added successfully. Please contact System Administrator.", custField.getLabel())));
        	
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;
        }    
        
    }  
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String deleteCustField(CustField custField) {
		try {
			Result result = this.getCustFieldService().deleteCustField(custField);
            
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				this.custFields = this.getCustFieldsByTarget(this.target);
	            
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Custom Field is deleted successfully", custField.getLabel())));
			}
			else if (result.getCode() == ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Custom Field is NOT deleted successfully. The field has been used already across the system. Please contact System Administrator.", custField.getLabel())));
			}
			
			return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Custom Field is NOT deleted successfully. Please contact System Administrator.", custField.getLabel())));
        	logger.error(e.getMessage(), e);
        	return ApplicationConstant.ERROR;       
        }    
	}
	
	private CustField buildCustFieldModel(CustField custField) {
		custField.setOrder(String.valueOf(this.getCustFieldService().getNewDisplayOrder(custField.getTarget())+1));

		return custField;
	}

	private void reset() {
		if (ValidateUtils.isObjectNotEmpty(this.custField)) {
			this.custField = new CustField();
		}
	}
	
	public void updateCustField(RowEditEvent event) {
        updateCustField((CustField)event.getObject());
    }
     
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<SelectItem> getTargets() {
		return targets;
	}

	public void setTargets(List<SelectItem> targets) {
		this.targets = targets;
	}

	public CustFieldService getCustFieldService() {
		return custFieldService;
	}

	public void setCustFieldService(CustFieldService custFieldService) {
		this.custFieldService = custFieldService;
	}

	public List<CustField> getCustFields() {
		return custFields;
	}

	public void setCustFields(List<CustField> custFields) {
		this.custFields = custFields;
	}

	public List<String> getCustFieldDisplayOrders() {
		return custFieldDisplayOrders;
	}

	public void setCustFieldDisplayOrders(List<String> custFieldDisplayOrders) {
		this.custFieldDisplayOrders = custFieldDisplayOrders;
	}


	public List<SelectItem> getCustFieldTypes() {
		return custFieldTypes;
	}

	public void setCustFieldTypes(List<SelectItem> custFieldTypes) {
		this.custFieldTypes = custFieldTypes;
	}

	public CustField getCustField() {
		return custField;
	}

	public void setCustField(CustField custField) {
		this.custField = custField;
	}
}
