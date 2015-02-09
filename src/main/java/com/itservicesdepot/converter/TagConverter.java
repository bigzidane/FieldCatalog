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

import com.itservicesdepot.model.Tag;
import com.itservicesdepot.service.TagService;

/**
 * This class is used in Add/Edit product pages
 *  
 * @author 	Nghia Do
 * @version 1.0
*/

@FacesConverter("tagConverter")
public class TagConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	TagService service = (TagService)FacesContextUtils.getWebApplicationContext(fc).getBean("tagService");
                Tag tag =  service.getNewTag(value);
                
                if (tag == null) {
                	tag = new Tag();
                	tag.setId(Integer.parseInt(value));
                	tag.setName("Item"+value);
                }
                
                return tag;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Tag."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Tag) object).getName());
        }
        else {
            return null;
        }
    }  
}  