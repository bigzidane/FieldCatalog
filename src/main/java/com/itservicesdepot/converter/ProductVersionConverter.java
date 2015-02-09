/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.jsf.FacesContextUtils;

import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class is used in Add/Edit product pages
 *  
 * @author 	Nghia Do
 * @version 1.0
*/

@FacesConverter("productVersionConverter")
public class ProductVersionConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0 && NumberUtils.isNumber(value)) {
            try {
            	VersionService service = (VersionService)FacesContextUtils.getWebApplicationContext(fc).getBean("versionService");
                return service.getProductVersionOnly(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid product version."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    	if(ValidateUtils.isObjectNotEmpty(object)) {
            return String.valueOf(((ProductVersion)object).getId());
        }
        else {
            return null;
        }
    }   
}    
