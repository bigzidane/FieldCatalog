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

import org.springframework.web.jsf.FacesContextUtils;

import com.itservicesdepot.model.Field;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class is used in Add/Edit field pages
 *  
 * @author 	Nghia Do
 * @version 1.0
*/

@FacesConverter("fieldConverter")
public class FieldConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	FieldService service = (FieldService)FacesContextUtils.getWebApplicationContext(fc).getBean("fieldService");
                return service.getField(value);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid field."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(ValidateUtils.isObjectNotEmpty(object)) {
            return String.valueOf(((Field)object).getName());
        }
        else {
            return null;
        }
    }   
}    
