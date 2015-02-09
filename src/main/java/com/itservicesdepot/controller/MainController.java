package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.AjaxRequestResult;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Role;
import com.itservicesdepot.model.User;
import com.itservicesdepot.model.fieldtag.FieldTag;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.RoleServiceImpl;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.ValidateUtils;

@Controller
public class MainController {
	
	@Autowired
    private FieldService fieldService;
	
	@Autowired
    private UserService userService;
	
	@RequestMapping(value = "verifyUser.action", method = RequestMethod.GET)
	public ModelAndView verifyUser(@RequestParam("uuid") String uuid)  {
		
		if (ValidateUtils.isObjectEmpty(uuid)) {
			return new ModelAndView("redirect:pages/unsecure/error.xhtml");
		}
		else {
			try {
				User user = this.userService.getUserByUUID(uuid);
				user.setIsApproved(ApplicationConstant.YES_INDICATOR);
				
				// add Viewer role
				Role role = new Role();
				role.setId(3);
				role.setRole(RoleServiceImpl.ROLE_VIEWWER);
				user.setRole(role);
				
				Set<User> userRoles = new HashSet<User>();
				userRoles.add(user);
				role.setUserRoles(userRoles);
				
				// update this user
				this.userService.updateUser(user);
				
				return new ModelAndView("redirect:pages/unsecure/login.xhtml");
			}
			catch (Exception e) {
				return new ModelAndView("redirect:pages/unsecure/error.xhtml");
			}
		}
	}
	
	@RequestMapping(value = "getFieldTags.action", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @ResponseBody
    public AjaxRequestResult<List<FieldTag>> getFieldTags(@RequestParam("photoID") String screenId) {
		final AjaxRequestResult<List<FieldTag>> response = new AjaxRequestResult<List<FieldTag>>();
        
        try {
            List<Field> fields= fieldService.getFieldsByScreenId(Integer.valueOf(screenId));
            
            final List<FieldTag> tags = new ArrayList<FieldTag>();
            for (Field field : fields) {
                if (field.getTagInfo() != null) {
                    if (field.getTagInfo().getTagHeight() > 0 && field.getTagInfo().getTagWidth() > 0) {
                        final FieldTag tag = new FieldTag();
                        tag.setFieldId(field.getId());
                        tag.setFieldName(field.getName());
                        tag.setTagInfo(field.getTagInfo());
                        
                        tags.add(tag);
                    }
                }
            }
            
            response.setSuccess(true);
            response.setResult(tags);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        
        return response;
	}
	
	@RequestMapping(value = "removeFieldLink.action", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public AjaxRequestResult<String> removeOutliner(@RequestParam String fieldId) {
        final AjaxRequestResult<String> response = new AjaxRequestResult<String>();

        try {
        	Field field= fieldService.getFieldOnly(Integer.valueOf(fieldId));
        	field.setTagInfo(null);
        	fieldService.updateField(field);

            response.setSuccess(true);
            response.setResult(String.valueOf(field.getId()));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }

        return response;
    }

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
}
