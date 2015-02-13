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
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.constant.NavigationConstant;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.EventValue;
import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenCustField;
import com.itservicesdepot.model.ScreenDocument;
import com.itservicesdepot.model.ScreenEvent;
import com.itservicesdepot.model.ScreenGroup;
import com.itservicesdepot.model.ScreenMessage;
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
 * This class handles Add/Edit/Update screen pages.
 *  
 * Only Admin/Designer can use those pages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="screenBean")
@ViewScoped
public class ScreenBean extends BaseBean {
	private static final long serialVersionUID = -1640765247412388946L;

	static Logger logger = Logger.getLogger(ScreenBean.class);
	
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
	
	@ManagedProperty("#{fileStorageService}")
    private FileStorageService fileStorageService;

	@ManagedProperty("#{sessionMgntBean}")
	private SessionMgntBean sessionMgntBean;
	
	@Transient
	private HtmlPanelGroup custFieldGroup;
	
	@Transient
	private TabView tabEvents;
	
	private List<User> owners;
	private List<Tag> suggestionTags;
	private UploadedFile attachedFile;
	
	private Map<String, Object> custFieldValues = new HashMap<String, Object>();
	private List<CustField> custFields;
	
	private Screen screen = new Screen();
	
	private DualListModel<Group> viewGroup;
	private DualListModel<Group> designGroup;
	
	private List<Product> products;
	private Product product;
    private List<ProductVersion> productVersions;
    private ProductVersion productVersion;
    
    private List<Screen> screens;
    private List<Screen> selectedParentScreens;
    
    // business rule tab
    private Map<String, EventValue> eventValues = new HashMap<String, EventValue>();
    private List<Event> events;
    
    // message tab
    private DualListModel<Message> messages;

    // for navigation view
    private TreeNode navigationRoot;
    
    private int paramId;
    
    private ScreenDocument selectedDocument;
    
	@PostConstruct
    public void init() {
		this.owners = this.userService.getUsers();
        this.suggestionTags = this.tagService.getSystemTags();
		this.custFieldValues = new HashMap<String, Object>();
		this.custFields = this.custFieldService.getCustFieldByScreen();
		this.products = this.productService.getProducts();
		this.eventValues = new HashMap<String, EventValue>();
		this.events = this.eventService.getEventByScreen();
		this.screen.setEffectiveDate(DateTimeUtils.Now());
		
		String screenId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(screenId)) {
			this.screen = this.screenService.getScreen(Integer.valueOf(screenId).intValue());
			this.screen.setFieldsCount(String.valueOf(this.fieldService.getFieldsCountByScreenId(this.screen.getId())));
			this.productVersions = this.versionService.getProductVersionsByProductId(this.screen.getProductVersion().getProduct().getId());
			
			this.product = this.screen.getProductVersion().getProduct();
			this.productVersion = this.screen.getProductVersion();
			
			this.screens = this.getParentScreens(this.screen.getProductVersion(), this.screen);
			this.selectedParentScreens = this.screenService.getParentIdScreens(this.screen.getParentId());
			
			this.initScreenGroup(screen);
			this.initScreenEvent(screen);
			this.initScreenCustField(screen);
			this.initScreenMessage(screen);
		}
		else {
			this.initScreenGroup(null);
			this.initScreenEvent(null);
			this.initScreenCustField(null);
			this.initScreenMessage(null);
		}
	}
	
	private void initScreenCustField(Screen screen) {
		if (ValidateUtils.isObjectEmpty(screen) || ValidateUtils.isObjectEmpty(screen.getScreenCustFields()) || screen.getScreenCustFields().size() == 0) {
			for (CustField custField : this.custFields) {
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				this.custFieldValues.put(id, "");
			}
		}
		else {
			for (ScreenCustField screenCustField : screen.getScreenCustFields()) {
				CustField custField = screenCustField.getCustField();
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				
				this.custFieldValues.put(id, screenCustField.getValue());
			}
		}
	}
	
	private void initScreenEvent(Screen screen) {
		if (ValidateUtils.isObjectEmpty(screen) || ValidateUtils.isObjectEmpty(screen.getScreenEvents()) || screen.getScreenEvents().size() == 0) {
			for (Event event : this.events) {
				String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
				this.eventValues.put(id, new EventValue());
			}
		}
		else {
			for (ScreenEvent screenEvent : screen.getScreenEvents()) {
				Event event = screenEvent.getEvent();
				String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
				
				EventValue eventValue = new EventValue(screenEvent.getBusinessRule(), screenEvent.getCodeRule());
				
				this.eventValues.put(id, eventValue);
			}
		}
	}
	
	private void initScreenGroup(Screen screen) {
		List<Group> sourceViewGroups = this.groupService.getGroups();
		List<Group> sourceDesignGroups = new ArrayList<Group>();
		
		List<Group> targetViewGroups = new ArrayList<Group>();
		List<Group> targetDesignGroups = new ArrayList<Group>();
		
		sourceDesignGroups.addAll(sourceViewGroups);
		
		// Edit mode
		if (ValidateUtils.isObjectNotEmpty(screen)) {
			List<ScreenGroup> screenGroups = screen.getScreenGroups();
			for (ScreenGroup screenGroup : screenGroups) {
				Group group = screenGroup.getGroup();
				
				if (ApplicationConstant.VIEW_LEVEL.equalsIgnoreCase(screenGroup.getLevel())) {
					targetViewGroups.add(group);
					sourceViewGroups.remove(group);
				}
				else if (ApplicationConstant.DESIGN_LEVEL.equalsIgnoreCase(screenGroup.getLevel())) {
					targetDesignGroups.add(group);
					sourceDesignGroups.remove(group);
				}
			}
		}
		
		this.viewGroup = new DualListModel<Group>(sourceViewGroups, targetViewGroups);
		this.designGroup = new DualListModel<Group>(sourceDesignGroups, targetDesignGroups);
	}
	
	private void initScreenMessage(Screen screen) {
		List<Message> sourceMessages = this.messageService.getMessages();
		List<Message> targetMessages = new ArrayList<Message>();
		
		// Edit mode
		if (ValidateUtils.isObjectNotEmpty(screen)) {
			List<ScreenMessage> screenMessages = screen.getScreenMessages();
			for (ScreenMessage screenMessage : screenMessages) {
				Message message = screenMessage.getMessage();
				
				targetMessages.add(message);
				sourceMessages.remove(message);
			}
		}
		
		this.messages = new DualListModel<Message>(sourceMessages, targetMessages);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public void addScreen() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				String fileNameSaved = fileStorageService.getFileName(ApplicationConstant.SCREEN_TARGET_KEY, String.valueOf(this.productVersion.getId()), this.screen.getName(), fAttachedFile.getName());
				this.screen.setAttachedFile(fileStorageService.storeFile(fileNameSaved, attachedFile.getInputstream()).get(0));
			}
            
            this.buildScreenModel(this.screen, currentUserId);
            this.buildScreenGroupModel(this.screen);
            this.buildScreenMessageModel(this.screen);
            this.buildScreenTagModel(this.screen);
            this.buildScreenCustFieldModel(this.screen);
            this.buildScreenEventModel(this.screen);
            
            Result result = screenService.addScreen(this.screen);
            if (result.getCode() == ErrorCodeConstant.SUCCESS) {
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' screen has been added to the system successfully.", screen.getName())));
            	this.reset();
            }
            else if (result.getCode() == ErrorCodeConstant.DUPLICATE_ENTRY) {
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' screen already exists.", screen.getName())));
            } 
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' screen has NOT been added to the system. Please contact System Administrator.",screen.getName())));
			logger.error(e.getMessage(),e);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String update(Screen screen) {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				String fileNameSaved = fileStorageService.getFileName(ApplicationConstant.SCREEN_TARGET_KEY, String.valueOf(this.productVersion.getId()), this.screen.getName(), fAttachedFile.getName());
				this.screen.setAttachedFile(fileStorageService.storeFile(fileNameSaved, attachedFile.getInputstream()).get(0));
			}
			
			this.buildScreenModel(screen, currentUserId);
			this.buildScreenTagModel(screen);
			this.buildScreenGroupModel(screen);
			this.buildScreenMessageModel(this.screen);
			this.buildScreenCustFieldModelEdit(screen);
			this.buildScreenEventModelEdit(screen);
			
			this.screenService.saveUpdateScreen(screen);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' screen has been updated to the system successfully.",screen.getName()))); 
			
			this.reset();
			
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' screen has NOT been updated to the system. Please contact System Administrator.",screen.getName())));
			logger.error(e.getMessage(),e);
			
			return ApplicationConstant.ERROR;
		}
	}
	
	private void buildScreenModel(Screen screen, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		if (screen.getId() <= 0) {
			screen.setCreatedDate(now);
			screen.setProductVersion(this.productVersion);
		}
		screen.setLastUpdatedDate(now);
		screen.setLastUpdatedUserId(currentUserId);
		
		String parentIds = "";
		if (ValidateUtils.isObjectNotEmpty(this.selectedParentScreens)) {
			parentIds += ApplicationConstant.COMMA_SEPARATOR;
			for (Screen parentScreen : this.selectedParentScreens) {
				parentIds += parentScreen.getId() + ApplicationConstant.COMMA_SEPARATOR; 
			}
			screen.setParentId(parentIds);
		}
		else {
			screen.setParentId(null);
		}
	}
		
	private void buildScreenTagModel(Screen screen) {
		HashMap<String, Tag> hs = new HashMap<String, Tag>();
		List<Tag> tags = screen.getScreenTags();
		
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
		screen.setScreenTags(newTags);
	}
	
	private void buildScreenGroupModel(Screen screen) {
		List<ScreenGroup> listScreenGroups = new ArrayList<ScreenGroup>();
		
		for (Group group : this.designGroup.getTarget()) {
		    ScreenGroup screenGroup = new ScreenGroup();
		    
		    screenGroup.setScreen(screen);
		    screenGroup.setGroup(group);
		    screenGroup.setLevel(ApplicationConstant.DESIGN_LEVEL);
		    
		    listScreenGroups.add(screenGroup);
		}
		
		for (Group group : this.viewGroup.getTarget()) {
		    ScreenGroup screenGroup = new ScreenGroup();
		    
		    screenGroup.setScreen(screen);
		    screenGroup.setGroup(group);
		    screenGroup.setLevel(ApplicationConstant.VIEW_LEVEL);
		    
		    // if the same group at the lower level, so we don't add that group again
		    if (listScreenGroups.contains(screenGroup)) continue;
		    
		    listScreenGroups.add(screenGroup);
		}
		
		screen.setScreenGroups(listScreenGroups);
	}
	
	private void buildScreenMessageModel(Screen screen) {
		List<ScreenMessage> listScreenMessages = new ArrayList<ScreenMessage>();
		
		for (Message message : this.messages.getTarget()) {
			ScreenMessage screenMessage = new ScreenMessage();
		    
			screenMessage.setScreen(screen);
			screenMessage.setMessage(message);
		    
			listScreenMessages.add(screenMessage);
		}
		
		screen.setScreenMessages(listScreenMessages);
	}
	
	private void buildScreenCustFieldModel(Screen screen) {
		List<ScreenCustField> listScreenCustFields = new ArrayList<ScreenCustField>();
		
		for (Map.Entry<String, Object> entry : this.custFieldValues.entrySet()) {
		    String[] keys = entry.getKey().split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String custFieldId = keys[keys.length-1];
		    
		    CustField custField = this.custFieldService.getCustField(Integer.valueOf(custFieldId).intValue());
		    ScreenCustField screenCustField = new ScreenCustField();
		    
		    screenCustField.setScreen(screen);
		    screenCustField.setCustField(custField);
		    screenCustField.setValue(ValidateUtils.isObjectNotEmpty(entry.getValue()) ? entry.getValue().toString() : "");
		    
		    listScreenCustFields.add(screenCustField);
		}
		
		screen.setScreenCustFields(listScreenCustFields);
	}
	
	private void buildScreenCustFieldModelEdit(Screen screen) {
		
		for (ScreenCustField screenCustField : screen.getScreenCustFields()) {
			CustField custField = screenCustField.getCustField();
			String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
			
			if (ValidateUtils.isObjectNotEmpty(this.custFieldValues.get(id))) {
				screenCustField.setValue(this.custFieldValues.get(id).toString());
			}
			else {
				screenCustField.setValue("");
			}
		}
	}
	
	private void buildScreenEventModel(Screen screen) {
		List<ScreenEvent> listScreenEvents = new ArrayList<ScreenEvent>();
		
		for (Map.Entry<String, EventValue> entry : this.eventValues.entrySet()) {
		    String[] keys = entry.getKey().split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String eventId = keys[keys.length-1];
		    
		    Event event = this.eventService.getEvent(Integer.valueOf(eventId).intValue());
		    ScreenEvent screenEvent = new ScreenEvent();
		    
		    screenEvent.setScreen(screen);
		    screenEvent.setEvent(event);
		    screenEvent.setEventValue((ValidateUtils.isObjectNotEmpty(entry.getValue()) ? entry.getValue() : null));
		    
		    listScreenEvents.add(screenEvent);
		}
		
		screen.setScreenEvents(listScreenEvents);
	}
	
	private void buildScreenEventModelEdit(Screen screen) {
		
		for (ScreenEvent screenEvent : screen.getScreenEvents()) {
			Event event = screenEvent.getEvent();
			String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
			
			screenEvent.setEventValue(this.eventValues.get(id));
		}
	}

	public void reset() {
		this.screen = null;
        if (ValidateUtils.isObjectNotEmpty(this.custFieldValues)) this.custFieldValues.clear();
        if (ValidateUtils.isObjectNotEmpty(this.eventValues)) this.eventValues.clear();
        
        this.product = null;
        this.productVersion = null;
        
        this.initScreenGroup(null);
        this.initScreenMessage(null);
        this.initScreenCustField(null);
    }
	
	public String showFields(Screen screen) {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_LIST_FIELDS;
	}
	
	public String showScreenImage() {
		this.setParamId(screen.getId());
		return NavigationConstant.NAV_TO_IMAGE;
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
        List<Tag> existingTags = this.screen.getScreenTags();
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
			this.screens = this.getParentScreens(this.productVersion, null);
		}
		else {
			this.screens = new ArrayList<Screen>();
		}
    }
	
	public List<Screen> getParentScreens(ProductVersion productVersion, Screen screen) {
		List<Screen> screens = new ArrayList<Screen>();
		
		screens = this.versionService.getScreensByProductVersionId(productVersion.getId());
		
		// remove itself from Parent list
		if (ValidateUtils.isObjectNotEmpty(screen) && screen.getId() > 0) {
			screens.remove(screen);
		}
		
		return screens; 
	}
	
	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
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

	/**
	 * This is to build Custom Field group dynamically
	 * @return
	 */
	public HtmlPanelGroup getCustFieldGroup() {
		if (ValidateUtils.isObjectEmpty(custFieldGroup)) {
			custFieldGroup = new HtmlPanelGroup();

			custFieldGroup.getChildren().addAll(ApplicationUtils.dynamicBuildCustFieldsComponent("screenBean", "custFieldValues", custFields, sessionMgntBean.hasInsufficientRoles("role_designer,role_admin")));
		}
		return custFieldGroup;
	}
	
	public TabView getTabEvents() {
		if (ValidateUtils.isObjectEmpty(this.tabEvents)) {
			this.tabEvents = new TabView();

			this.tabEvents.getChildren().addAll(ApplicationUtils.dynamicBuildEventsComponent("screenBean", "eventValues", this.events, sessionMgntBean.hasInsufficientRoles("role_designer,role_admin")));
		}
		return tabEvents;
	}
	
	public void setCustFieldGroup(HtmlPanelGroup custFieldGroup) {
		this.custFieldGroup = custFieldGroup;
	}

	public boolean getCustFieldAvailable() {
		List<CustField> list = custFieldService.getCustFieldByScreen();
		return ValidateUtils.isObjectNotEmpty(list) ? true : false;
	}

	public void buildNavigationMap() {
		if (ValidateUtils.isObjectEmpty(this.navigationRoot)) {
			this.navigationRoot = new DefaultTreeNode(this.screen.getName(), null);
			navigationRoot.setExpanded(true);
			
			this.buildNav(navigationRoot, this.screenService.getParentScreens(this.screen.getParentId()));
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

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
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

	public DualListModel<Message> getMessages() {
		return messages;
	}

	public void setMessages(DualListModel<Message> messages) {
		this.messages = messages;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	public SessionMgntBean getSessionMgntBean() {
		return sessionMgntBean;
	}

	public void setSessionMgntBean(SessionMgntBean sessionMgntBean) {
		this.sessionMgntBean = sessionMgntBean;
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
		this.paramId = paramId;
	}
	
	public boolean getEventsAvailable() {
		return ValidateUtils.isObjectNotEmpty(this.events) ? true : false;
	}

	public List<Screen> getSelectedParentScreens() {
		return selectedParentScreens;
	}

	public void setSelectedParentScreens(List<Screen> selectedParentScreens) {
		this.selectedParentScreens = selectedParentScreens;
	}

	public TreeNode getNavigationRoot() {
		return navigationRoot;
	}

	public void setNavigationRoot(TreeNode navigationRoot) {
		this.navigationRoot = navigationRoot;
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
	
	public void setSelectedDocument(ScreenDocument document) {
		this.selectedDocument = document;
	}
}