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

import com.itservicesdepot.model.Screen;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class is used in Add/Edit screen pages
 *  
 * @author 	Nghia Do
 * @version 1.0
*/

@FacesConverter("screenConverter")
public class ScreenConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0 && NumberUtils.isNumber(value)) {
            try {
            	ScreenService service = (ScreenService)FacesContextUtils.getWebApplicationContext(fc).getBean("screenService");
                return service.getScreenOnly(Integer.valueOf(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid screen."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(ValidateUtils.isObjectNotEmpty(object)) {
            return String.valueOf(((Screen)object).getId());
        }
        else {
            return null;
        }
    }   
}    
