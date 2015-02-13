/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.TabView;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.EventValue;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldCustField;
import com.itservicesdepot.model.FieldDocument;
import com.itservicesdepot.model.FieldEvent;
import com.itservicesdepot.model.FieldGroup;
import com.itservicesdepot.model.FieldMessage;
import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.Tag;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.CustFieldService;
import com.itservicesdepot.service.EventService;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.service.GroupService;
import com.itservicesdepot.service.MessageService;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.TagService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Add/Edit/Update field pages.
 *  
 * Only Admin/Designer can use those pages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="fieldBean")
@ViewScoped
public class FieldBean extends BaseBean {
	private static final long serialVersionUID = 5845231958440169623L;

	static Logger logger = Logger.getLogger(FieldBean.class);
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{tagService}")
    private TagService tagService;
	
	@ManagedProperty("#{custFieldService}")
    private CustFieldService custFieldService;
	
	@ManagedProperty("#{eventService}")
    private EventService eventService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{groupService}")
    private GroupService groupService;
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{messageService}")
    private MessageService messageService;
	
	@ManagedProperty("#{sessionMgntBean}")
	private SessionMgntBean sessionMgntBean;

	@ManagedProperty("#{fileStorageService}")
    private FileStorageService fileStorageService;
	
	@Transient
	private HtmlPanelGroup custFieldGroup;
	
	@Transient
	private TabView tabEvents;
	
	private List<User> owners;
	private List<Tag> suggestionTags;
	private UploadedFile attachedFile;
	
	private Map<String, Object> custFieldValues = new HashMap<String, Object>();
	private List<CustField> custFields;
	
	// business rule tab
    private Map<String, EventValue> eventValues = new HashMap<String, EventValue>();
    private List<Event> events;
    
	private Field field = new Field();
	
	private DualListModel<Group> viewGroup;
	private DualListModel<Group> designGroup;
	
	private List<Product> products;
	private Product product;
	
    private List<ProductVersion> productVersions;
    private ProductVersion productVersion;
    
    private List<Screen> screens;
    private Screen screen;
    
    private List<Field> selectedDependentFields;
    private List<Field> dependentFields;

    // message tab
    private DualListModel<Message> messages;
    
    // for navigation view
    private TreeNode navigationRoot;
    
    private FieldDocument selectedDocument;
    
	@PostConstruct
    public void init() {
		this.owners = this.userService.getUsers();
        this.suggestionTags = this.tagService.getSystemTags();
		this.custFieldValues = new HashMap<String, Object>();
		this.custFields = this.custFieldService.getCustFieldByField();
		this.products = this.productService.getProducts();
		this.eventValues = new HashMap<String, EventValue>();
		this.events = this.eventService.getEventByField();
		this.field.setEffectiveDate(DateTimeUtils.Now());
		
		String fieldId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(fieldId)) {
			this.field = this.fieldService.getField(Integer.valueOf(fieldId).intValue());
			
			this.selectedDependentFields = this.fieldService.getDependentIdFields(this.field.getDependentId());
			this.dependentFields = this.fieldService.getFieldsByScreenId(this.field.getScreen().getId());
			this.dependentFields.remove(this.field);
			
			this.initFieldGroup(field);
			this.initFieldEvent(field);
			this.initFieldCustField(field);
			this.initFieldMessage(field);
		}
		else {
			this.initFieldGroup(null);
			this.initFieldEvent(null);
			this.initFieldCustField(null);
			this.initFieldMessage(null);
		}
	}
	
	private void initFieldCustField(Field field) {
		if (ValidateUtils.isObjectEmpty(field) || ValidateUtils.isObjectEmpty(field.getFieldCustFields()) || field.getFieldCustFields().size() == 0) {
			for (CustField custField : this.custFields) {
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				this.custFieldValues.put(id, "");
			}
		}
		else {
			for (FieldCustField fieldCustField : field.getFieldCustFields()) {
				CustField custField = fieldCustField.getCustField();
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				
				this.custFieldValues.put(id, fieldCustField.getValue());
			}
		}
	}
	
	private void initFieldEvent(Field field) {
		if (ValidateUtils.isObjectEmpty(field) || ValidateUtils.isObjectEmpty(this.field.getFieldEvents()) || this.field.getFieldEvents().size() == 0) {
			for (Event event : this.events) {
				String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
				this.eventValues.put(id, new EventValue());
			}
		}
		else {
			for (FieldEvent fieldEvent : this.field.getFieldEvents()) {
				Event event = fieldEvent.getEvent();
				String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
				
				EventValue eventValue = new EventValue(fieldEvent.getBusinessRule(), fieldEvent.getCodeRule());
				
				this.eventValues.put(id, eventValue);
			}
		}
	}
	
	private void initFieldGroup(Field field) {
		List<Group> sourceViewGroups = this.groupService.getGroups();
		List<Group> sourceDesignGroups = new ArrayList<Group>();
		
		List<Group> targetViewGroups = new ArrayList<Group>();
		List<Group> targetDesignGroups = new ArrayList<Group>();
		
		sourceDesignGroups.addAll(sourceViewGroups);
		
		// Edit mode
		if (ValidateUtils.isObjectNotEmpty(field)) {
			List<FieldGroup> fieldGroups = field.getFieldGroups();
			for (FieldGroup fieldGroup : fieldGroups) {
				Group group = fieldGroup.getGroup();
				
				if (ApplicationConstant.VIEW_LEVEL.equalsIgnoreCase(fieldGroup.getLevel())) {
					targetViewGroups.add(group);
					sourceViewGroups.remove(group);
				}
				else if (ApplicationConstant.DESIGN_LEVEL.equalsIgnoreCase(fieldGroup.getLevel())) {
					targetDesignGroups.add(group);
					sourceDesignGroups.remove(group);
				}
			}
		}
		
		this.viewGroup = new DualListModel<Group>(sourceViewGroups, targetViewGroups);
		this.designGroup = new DualListModel<Group>(sourceDesignGroups, targetDesignGroups);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addField() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				this.field.setAttachedFile(fileStorageService.storeFile(fAttachedFile.getName(), attachedFile.getInputstream()).get(0));
			}
            
			this.buildFieldModel(this.field, currentUserId, this.screen);
			this.buildFieldGroupModel(this.field);
            this.buildFieldMessageModel(field);
            this.buildFieldTagModel(this.field);
            this.buildFieldCustFieldModel(this.field);
            this.buildFieldEventModel(this.field);
			
            fieldService.addField(this.field);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' field has been added to the system successfully.", field.getName())));
		
			this.reset();
			
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' field has NOT been added to the system. Please contact System Administrator.",field.getName())));
			logger.error(e.getMessage(),e);
			
			return ApplicationConstant.ERROR;
		}
	}
	
	private void initFieldMessage(Field field) {
		List<Message> sourceMessages = this.messageService.getMessages();
		List<Message> targetMessages = new ArrayList<Message>();
		
		// Edit mode
		if (ValidateUtils.isObjectNotEmpty(field)) {
			List<FieldMessage> fieldMessages = field.getFieldMessages();
			for (FieldMessage fieldMessage : fieldMessages) {
				Message message = fieldMessage.getMessage();
				
				targetMessages.add(message);
				sourceMessages.remove(message);
			}
		}
		
		this.messages = new DualListModel<Message>(sourceMessages, targetMessages);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String update(Field field) {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				this.field.setAttachedFile(fileStorageService.storeFile(fAttachedFile.getName(), attachedFile.getInputstream()).get(0));
			}
			
			Field dbField = this.fieldService.getField(field.getId());
			
			this.buildFieldModel(field, currentUserId, null);
			this.buildFieldTagModelEdit(field, dbField);
			this.buildFieldGroupModel(field);
			this.buildFieldMessageModel(field);
			this.buildFieldCustFieldModelEdit(field);
			this.buildFieldEventModelEdit(field);
			
			this.fieldService.updateField(field);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' field has been updated to the system successfully.",field.getName())));
			
			this.reset();
			
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' field has NOT been updated to the system. Please contact System Administrator.",field.getName())));
			logger.error(e.getMessage(),e);
			
			return ApplicationConstant.ERROR;
		}
	}
	
	protected void buildFieldModel(Field field, int currentUserId, Screen screen) {
		Date now = DateTimeUtils.Now();
		
		if (field.getId() <= 0) {
			field.setCreatedDate(now);
			field.setScreen(screen);
		}
		field.setLastUpdatedDate(now);
		field.setLastUpdatedUserId(currentUserId);
		
		String dependentIds = "";
		if (ValidateUtils.isObjectNotEmpty(this.selectedDependentFields)) {
			dependentIds += ApplicationConstant.COMMA_SEPARATOR;
			for (Field dfield : this.selectedDependentFields) {
				dependentIds += dfield.getId() + ApplicationConstant.COMMA_SEPARATOR; 
			}
			field.setDependentId(dependentIds);
		}
		else {
			field.setDependentId(null);
		}
	}
	
	private void buildFieldTagModel(Field field) {
		HashMap<String, Tag> hs = new HashMap<String, Tag>();
		List<Tag> tags = field.getFieldTags();
		
		if (ValidateUtils.isObjectEmpty(tags)) return;
		
		for (Tag newTag : tags) {
			if (hs.containsKey(newTag.getName())) continue;
			
			Tag existingTag = this.tagService.getTag(newTag.getName());
			if (ValidateUtils.isObjectNotEmpty(existingTag)) {
				hs.put(newTag.getName(), existingTag);
			}
			else {
				hs.put(newTag.getName(), newTag);
			}
		}
		
		List<Tag> newTags = new ArrayList<Tag>(hs.values());
		field.setFieldTags(newTags);
	}
	
	private void buildFieldTagModelEdit(Field field, Field dbField) {
		HashMap<String, Tag> hs = new HashMap<String, Tag>();
		List<Tag> existingTags = dbField.getFieldTags();
		
		if (ValidateUtils.isObjectEmpty(existingTags)) return;
		
		for (Tag newTag : existingTags) {
			if (hs.containsKey(newTag.getName())) continue;
			
			Tag existingTag = this.tagService.getTag(newTag.getName());
			if (ValidateUtils.isObjectNotEmpty(existingTag)) {
				hs.put(newTag.getName(), existingTag);
			}
			else {
				hs.put(newTag.getName(), newTag);
			}
		}
		
		List<Tag> newTags = new ArrayList<Tag>(hs.values());
		field.setFieldTags(newTags);
	}
	
	private void buildFieldGroupModel(Field field) {
		List<FieldGroup> listFieldGroups = new ArrayList<FieldGroup>();
		
		for (Group group : this.designGroup.getTarget()) {
		    FieldGroup fieldGroup = new FieldGroup();
		    
		    fieldGroup.setField(field);
		    fieldGroup.setGroup(group);
		    fieldGroup.setLevel(ApplicationConstant.DESIGN_LEVEL);
		    
		    listFieldGroups.add(fieldGroup);
		}
		
		for (Group group : this.viewGroup.getTarget()) {
		    FieldGroup fieldGroup = new FieldGroup();
		    
		    fieldGroup.setField(field);
		    fieldGroup.setGroup(group);
		    fieldGroup.setLevel(ApplicationConstant.VIEW_LEVEL);
		    
		    // if the same group at the lower level, so we don't add that group again
		    if (listFieldGroups.contains(fieldGroup)) continue;
		    
		    listFieldGroups.add(fieldGroup);
		}
		
		field.setFieldGroups(listFieldGroups);
	}
	
	private void buildFieldMessageModel(Field field) {
		List<FieldMessage> listFieldMessages = new ArrayList<FieldMessage>();
		
		for (Message message : this.messages.getTarget()) {
			FieldMessage fieldMessage = new FieldMessage();
		    
			fieldMessage.setField(field);
			fieldMessage.setMessage(message);
		    
			listFieldMessages.add(fieldMessage);
		}
		
		field.setFieldMessages(listFieldMessages);
	}
	
	private void buildFieldCustFieldModel(Field field) {
		List<FieldCustField> listFieldCustFields = new ArrayList<FieldCustField>();
		
		for (Map.Entry<String, Object> entry : this.custFieldValues.entrySet()) {
		    String[] keys = entry.getKey().split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String custFieldId = keys[keys.length-1];
		    
		    CustField custField = this.custFieldService.getCustField(Integer.valueOf(custFieldId).intValue());
		    FieldCustField fieldCustField = new FieldCustField();
		    
		    fieldCustField.setField(field);
		    fieldCustField.setCustField(custField);
		    fieldCustField.setValue(ValidateUtils.isObjectNotEmpty(entry.getValue()) ? entry.getValue().toString() : "");
		    
		    listFieldCustFields.add(fieldCustField);
		}
		
		field.setFieldCustFields(listFieldCustFields);
	}
	
	private void buildFieldCustFieldModelEdit(Field field) {
		if (ValidateUtils.isObjectNotEmpty(field.getFieldCustFields())) {
			for (FieldCustField fieldCustField : field.getFieldCustFields()) {
				CustField custField = fieldCustField.getCustField();
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				
				if (ValidateUtils.isObjectNotEmpty(this.custFieldValues.get(id))) {
					fieldCustField.setValue(this.custFieldValues.get(id).toString());
				}
				else {
					fieldCustField.setValue("");
				}
			}
		}
		else {
			buildFieldCustFieldModel(field);
		}
	}

	private void buildFieldEventModel(Field field) {
		List<FieldEvent> listFieldEvents = new ArrayList<FieldEvent>();
		
		for (Map.Entry<String, EventValue> entry : this.eventValues.entrySet()) {
		    String[] keys = entry.getKey().split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String eventId = keys[keys.length-1];
		    
		    Event event = this.eventService.getEvent(Integer.valueOf(eventId).intValue());
		    FieldEvent fieldEvent = new FieldEvent();
		    
		    fieldEvent.setField(field);
		    fieldEvent.setEvent(event);
		    fieldEvent.setEventValue((ValidateUtils.isObjectNotEmpty(entry.getValue()) ? entry.getValue() : null));
		    
		    listFieldEvents.add(fieldEvent);
		}
		
		field.setFieldEvents(listFieldEvents);
	}
	
	private void buildFieldEventModelEdit(Field field) {
		if (ValidateUtils.isObjectNotEmpty(field.getFieldEvents())) {
			for (FieldEvent fieldEvent : field.getFieldEvents()) {
				Event event = fieldEvent.getEvent();
				String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
				
				fieldEvent.setEventValue(this.eventValues.get(id));
			}
		}
		else {
			buildFieldEventModel(field);
		}
	}
	
	public void reset() {
		this.field = null;
        if (ValidateUtils.isObjectNotEmpty(this.custFieldValues)) this.custFieldValues.clear();
        if (ValidateUtils.isObjectNotEmpty(this.eventValues)) this.eventValues.clear();
        
        this.product = null;
        this.productVersion = null;
        this.screen = null;
        
        this.initFieldGroup(null);
        this.initFieldMessage(null);
        this.initFieldCustField(null);
    }
	
	public List<Tag> completeTag(String tagName) {
        List<Tag> filteredTags = new ArrayList<Tag>();
        
        for (int i = 0; i < this.suggestionTags.size(); i++) {
            Tag tag = this.suggestionTags.get(i);
            if(tag.getName().toLowerCase().contains(tagName)) {
            	filteredTags.add(tag);
            }
        }
        
        if (filteredTags.isEmpty()) {
        	filteredTags.add(tagService.getNewTag(tagName));
        }
        return filteredTags;
    }
	
	public List<Tag> completeTagEdit(String tagName) {
        List<Tag> allTags = tagService.getSystemTags();
        List<Tag> existingTags = this.field.getFieldTags();
        List<Tag> filteredTags = new ArrayList<Tag>();
        
        boolean existing = false;
        for (Tag existingTag : existingTags) {
        	if (existingTag.getName().contains(tagName)) {
        		existing = true;
        		break;
        	}
        }
        
        if (existing) {
        	return filteredTags;
        }
        
        for (int i = 0; i < allTags.size(); i++) {
            Tag tag = allTags.get(i);
            
            if (tag.getName().toLowerCase().contains(tagName)) {
            	filteredTags.add(tag);
            }
        }
        
        if (filteredTags.isEmpty()) {
        	filteredTags.add(tagService.getNewTag(tagName));
        }
        return filteredTags;
    }
		
	public void onProductChange() {
		if (ValidateUtils.isObjectNotEmpty(this.product)) {
			this.productVersions = this.versionService.getProductVersionsByProductId(this.product.getId());
		}
		else {
			this.productVersions = new ArrayList<ProductVersion>();
		}
    }
	
	public void onProductVersionChange() {
		if (ValidateUtils.isObjectNotEmpty(this.productVersion)) {
			this.screens = this.versionService.getScreensByProductVersionId(this.productVersion.getId());
		}
		else {
			this.screens = new ArrayList<Screen>();
		}
    }
	
	public void onScreenChange() {
		if (ValidateUtils.isObjectNotEmpty(this.screen)) {
			this.dependentFields = this.fieldService.getFieldsByScreenId(this.screen.getId());
		}
		else {
			this.dependentFields = new ArrayList<Field>();
		}
    }
	
	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TagService getTagService() {
		return tagService;
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public UploadedFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(UploadedFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public CustFieldService getCustFieldService() {
		return custFieldService;
	}

	public void setCustFieldService(CustFieldService custFieldService) {
		this.custFieldService = custFieldService;
	}

	public HtmlPanelGroup getCustFieldGroup() {
		if (ValidateUtils.isObjectEmpty(custFieldGroup)) {
			custFieldGroup = new HtmlPanelGroup();
			
			custFieldGroup.getChildren().addAll(ApplicationUtils.dynamicBuildCustFieldsComponent("fieldBean", "custFieldValues", custFields, sessionMgntBean.hasInsufficientRoles("role_designer,role_admin")));
		}
		return custFieldGroup;
	}
	
	public TabView getTabEvents() {
		if (ValidateUtils.isObjectEmpty(this.tabEvents)) {
			this.tabEvents = new TabView();

			this.tabEvents.getChildren().addAll(ApplicationUtils.dynamicBuildEventsComponent("fieldBean", "eventValues", this.events, sessionMgntBean.hasInsufficientRoles("role_designer,role_admin")));
		}
		return tabEvents;
	}
	
	public void setCustFieldGroup(HtmlPanelGroup custFieldGroup) {
		this.custFieldGroup = custFieldGroup;
	}

	public boolean getCustFieldAvailable() {
		List<CustField> list = custFieldService.getCustFieldByField();
		return ValidateUtils.isObjectNotEmpty(list) ? true : false;
	}
	
	public void buildNavigationMap() {
		if (ValidateUtils.isObjectEmpty(this.navigationRoot)) {
			this.navigationRoot = new DefaultTreeNode(this.field.getName(), null);
			navigationRoot.setExpanded(true);
			
			TreeNode directScreen = new DefaultTreeNode(this.field.getScreen().getName(), navigationRoot);
			directScreen.setExpanded(true);
			
			this.buildNav(directScreen, this.screenService.getParentScreens(this.field.getScreen().getParentId()));
		}
	}
	
	private void buildNav(TreeNode currentNode, List<Screen> screens) {
		if (ValidateUtils.isObjectNotEmpty(screens)) {
			for (Screen pS : screens) {
				TreeNode node = new DefaultTreeNode(pS.getName(), currentNode);
		        node.setExpanded(true);
		        
		        this.buildNav(node, this.screenService.getParentScreens(pS.getParentId()));
			}
		}
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Map<String, Object> getCustFieldValues() {
		return custFieldValues;
	}

	public void setCustFieldValues(Map<String, Object> custFieldValues) {
		this.custFieldValues = custFieldValues;
	}

	public DualListModel<Group> getViewGroup() {
		return viewGroup;
	}

	public void setViewGroup(DualListModel<Group> viewGroup) {
		this.viewGroup = viewGroup;
	}

	public DualListModel<Group> getDesignGroup() {
		return designGroup;
	}

	public void setDesignGroup(DualListModel<Group> designGroup) {
		this.designGroup = designGroup;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public List<Tag> getSystemTags() {
		return suggestionTags;
	}

	public void setSystemTags(List<Tag> systemTags) {
		this.suggestionTags = systemTags;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<ProductVersion> getVersions() {
		return productVersions;
	}

	public void setVersions(List<ProductVersion> versions) {
		this.productVersions = versions;
	}

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductVersion> getProductVersions() {
		return productVersions;
	}

	public void setProductVersions(List<ProductVersion> productVersions) {
		this.productVersions = productVersions;
	}

	public List<Screen> getScreens() {
		return screens;
	}

	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public List<CustField> getCustFields() {
		return custFields;
	}

	public void setCustFields(List<CustField> custFields) {
		this.custFields = custFields;
	}

	public Map<String, EventValue> getEventValues() {
		return eventValues;
	}

	public void setEventValues(Map<String, EventValue> eventValues) {
		this.eventValues = eventValues;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void setTabEvents(TabView tabEvents) {
		this.tabEvents = tabEvents;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public DualListModel<Message> getMessages() {
		return messages;
	}

	public void setMessages(DualListModel<Message> messages) {
		this.messages = messages;
	}

	public SessionMgntBean getSessionMgntBean() {
		return sessionMgntBean;
	}

	public void setSessionMgntBean(SessionMgntBean sessionMgntBean) {
		this.sessionMgntBean = sessionMgntBean;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}
	
	public boolean getEventsAvailable() {
		return ValidateUtils.isObjectNotEmpty(this.events) ? true : false;
	}

	public List<Field> getDependentFields() {
		return dependentFields;
	}

	public void setDependentFields(List<Field> dependentFields) {
		this.dependentFields = dependentFields;
	}

	public List<Field> getSelectedDependentFields() {
		return selectedDependentFields;
	}

	public void setSelectedDependentFields(List<Field> selectedDependentFields) {
		this.selectedDependentFields = selectedDependentFields;
	}

	public TreeNode getNavigationRoot() {
		return navigationRoot;
	}

	public void setNavigationRoot(TreeNode navigationRoot) {
		this.navigationRoot = navigationRoot;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}
	
	public StreamedContent getFileDownload() {
		try {
			StreamedContent file = this.fileStorageService.getStreamedContent(this.selectedDocument.getFile(), this.selectedDocument.getName());
	        
	        return file;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' file is NOT found. Please contact System Administrator.",this.selectedDocument.getName())));
			logger.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public void setSelectedDocument(FieldDocument document) {
		this.selectedDocument = document;
	}
}