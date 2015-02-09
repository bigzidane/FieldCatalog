/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.validator;
 
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.service.VersionService;
 
@Component("productVersionValidator")
public class ProductVersionValidator implements Validator, ClientValidator {
 
	@Autowired
    private VersionService versionService;
	
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null) {
            return;
        }
        
        String versionName = (String)value;
        if (versionName.trim().isEmpty()) {
        	throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Value is required."));
        }
    
        int id=(Integer) component.getAttributes().get("productId"); 

        List<ProductVersion> productVersions = this.versionService.getProductVersionsByProductId(id);
        for (ProductVersion existingVersion : productVersions) {
        	if (versionName.equalsIgnoreCase(existingVersion.getName())) {
        		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "'" + value + "' is a duplicated product version."));	
        	}
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "productVersionValidator";
    }

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}     
}