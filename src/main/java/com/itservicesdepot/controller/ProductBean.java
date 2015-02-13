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

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductCustField;
import com.itservicesdepot.model.ProductGroup;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.ProductVersionDocument;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Tag;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.CustFieldService;
import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.service.GroupService;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.TagService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Add/Edit/Update product pages.
 *  
 * Only Admin/Designer can use those pages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="productBean")
@ViewScoped
public class ProductBean extends BaseBean {
	private static final long serialVersionUID = -1640765247412388946L;

	static Logger logger = Logger.getLogger(ProductBean.class);
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{tagService}")
    private TagService tagService;
	
	@ManagedProperty("#{custFieldService}")
    private CustFieldService custFieldService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	@ManagedProperty("#{groupService}")
    private GroupService groupService;

	@ManagedProperty("#{sessionMgntBean}")
	private SessionMgntBean sessionMgntBean;
	
	@ManagedProperty("#{fileStorageService}")
    private FileStorageService fileStorageService;
	
	private HtmlPanelGroup custFieldGroup;
	
	private List<User> owners;
	private List<Tag> suggestionTags;
	private UploadedFile attachedFile;
	
	private Map<String, Object> custFieldValues = new HashMap<String, Object>();
	private List<CustField> custFields;
	
	private ProductVersion productVersion = new ProductVersion();
	
	private DualListModel<Group> viewGroup;
	private DualListModel<Group> designGroup;

	private ProductVersionDocument selectedDocument;
	
	@PostConstruct
    public void init() {
		this.owners = this.userService.getUsers();
        this.suggestionTags = this.tagService.getSystemTags();		// In Product pages (Add/Update), we suggest Tags which belong to system.
		this.custFieldValues = new HashMap<String, Object>();
		this.custFields = this.custFieldService.getCustFieldByProduct();
		this.productVersion.getProduct().setEffectiveDate(DateTimeUtils.Now());
		
		String productVersionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(productVersionId)) {
			this.productVersion = this.versionService.getProductVersionOnly(Integer.valueOf(productVersionId));
			
			this.initProductGroup(this.productVersion.getProduct());
			this.initFieldCustField(this.productVersion.getProduct());
		}
		else {
			this.initProductGroup(null);
			this.initFieldCustField(null);
		}
	}
	
	private void initFieldCustField(Product product) {
		if (ValidateUtils.isObjectEmpty(product) || ValidateUtils.isObjectEmpty(product.getProductCustFields()) || product.getProductCustFields().size() == 0) {
			for (CustField custField : this.custFields) {
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				this.custFieldValues.put(id, "");
			}
		}
		else {
			for (ProductCustField productCustField : product.getProductCustFields()) {
				CustField custField = productCustField.getCustField();
				String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
				
				this.custFieldValues.put(id, productCustField.getValue());
			}
		}
	}
	
	private void initProductGroup(Product product) {
		List<Group> sourceViewGroups = this.groupService.getGroups();
		List<Group> sourceDesignGroups = new ArrayList<Group>();
		
		List<Group> targetViewGroups = new ArrayList<Group>();
		List<Group> targetDesignGroups = new ArrayList<Group>();
		
		sourceDesignGroups.addAll(sourceViewGroups);
		
		// Edit mode
		if (ValidateUtils.isObjectNotEmpty(product)) {
			List<ProductGroup> productGroups = product.getProductGroups();
			for (ProductGroup productGroup : productGroups) {
				Group group = productGroup.getGroup();
				
				if (ApplicationConstant.VIEW_LEVEL.equalsIgnoreCase(productGroup.getLevel())) {
					targetViewGroups.add(group);
					sourceViewGroups.remove(group);
				}
				else if (ApplicationConstant.DESIGN_LEVEL.equalsIgnoreCase(productGroup.getLevel())) {
					targetDesignGroups.add(group);
					sourceDesignGroups.remove(group);
				}
			}
		}
		
		this.viewGroup = new DualListModel<Group>(sourceViewGroups, targetViewGroups);
		this.designGroup = new DualListModel<Group>(sourceDesignGroups, targetDesignGroups);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public void addProduct() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				this.productVersion.getProduct().setAttachedFile(fileStorageService.storeFile(fAttachedFile.getName(), attachedFile.getInputstream()).get(0));
			}
            
            buildProductModel(this.productVersion.getProduct(), currentUserId);
            buildProductVersionModel(this.productVersion.getProduct());
            buildProductGroupModel(this.productVersion.getProduct());
            buildProductTagModel(this.productVersion.getProduct());
            buildProductCustFieldModel(this.productVersion.getProduct());
			
            Result result = productService.addProduct(this.productVersion.getProduct());
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' product has been added to the system successfully.", this.productVersion.getProduct().getName())));
				this.reset();
			}
			else if (result.getCode() == ErrorCodeConstant.DUPLICATE_ENTRY) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' product and version '%s' already exists.", this.productVersion.getProduct().getName(), this.productVersion.getName())));
			}
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' product has NOT been added to the system. Please contact System Administrator.",this.productVersion.getProduct().getName())));
			logger.error(e.getMessage(),e);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String update(Product product) {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			if (attachedFile != null) {
				File fAttachedFile = new File(attachedFile.getFileName());
				this.productVersion.getProduct().setAttachedFile(fileStorageService.storeFile(fAttachedFile.getName(), attachedFile.getInputstream()).get(0));
			}
			
			this.buildProductModelEdit(product, currentUserId);
			this.buildProductTagModel(product);
			this.buildProductGroupModel(product);
			this.buildProductCustFieldModelEdit(product);
			
			this.productService.updateProduct(product);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' product has been updated to the system successfully.",product.getName())));
			
			this.reset();
			
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' product has NOT been updated to the system. Please contact System Administrator.",product.getName())));
			logger.error(e.getMessage(),e);
			
			return ApplicationConstant.ERROR;
		}
	}
	
	private void buildProductModel(Product product, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		product.setCreatedDate(now);
		product.setLastUpdatedDate(now);
		product.setLastUpdatedUserId(currentUserId);
	}
	
	private void buildProductModelEdit(Product product, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		product.setLastUpdatedDate(now);
		product.setLastUpdatedUserId(currentUserId);
	}
	
	private void buildProductVersionModel(Product product) {
		List<ProductVersion> productVersions = new ArrayList<ProductVersion>();
		
		ProductVersion productVersion = new ProductVersion();
		
		productVersion.setProduct(product);
		productVersion.setName(this.productVersion.getName());
		
		productVersions.add(productVersion);
		
		product.setProductVersion(productVersions);
	}
	
	private void buildProductTagModel(Product product) {
		HashMap<String, Tag> hs = new HashMap<String, Tag>();
		List<Tag> tags = product.getProductTags();
		
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
		product.setProductTags(newTags);
	}
	
	private void buildProductGroupModel(Product product) {
		List<ProductGroup> listProductGroups = new ArrayList<ProductGroup>();
		
		for (Group group : this.designGroup.getTarget()) {
		    ProductGroup productGroup = new ProductGroup();
		    
		    productGroup.setProduct(product);
		    productGroup.setGroup(group);
		    productGroup.setLevel(ApplicationConstant.DESIGN_LEVEL);
		    
		    listProductGroups.add(productGroup);
		}
		
		for (Group group : this.viewGroup.getTarget()) {
		    ProductGroup productGroup = new ProductGroup();
		    
		    productGroup.setProduct(product);
		    productGroup.setGroup(group);
		    productGroup.setLevel(ApplicationConstant.VIEW_LEVEL);
		    
		    // if the same group at the lower level, so we don't add that group again
		    if (listProductGroups.contains(productGroup)) continue;
		    
		    listProductGroups.add(productGroup);
		}
		
		product.setProductGroups(listProductGroups);
	}
	
	private void buildProductCustFieldModel(Product product) {
		List<ProductCustField> listProductCustFields = new ArrayList<ProductCustField>();
		
		for (Map.Entry<String, Object> entry : this.custFieldValues.entrySet()) {
		    String[] keys = entry.getKey().split(ApplicationConstant.CUST_FIELD_SEPARATOR);
		    String custFieldId = keys[keys.length-1];
		    
		    CustField custField = this.custFieldService.getCustField(Integer.valueOf(custFieldId).intValue());
		    ProductCustField productCustField = new ProductCustField();
		    
		    productCustField.setProduct(product);
		    productCustField.setCustField(custField);
		    productCustField.setValue(ValidateUtils.isObjectNotEmpty(entry.getValue()) ? entry.getValue().toString() : "");
		    
		    listProductCustFields.add(productCustField);
		}
		
		product.setProductCustFields(listProductCustFields);
	}
	
	private void buildProductCustFieldModelEdit(Product product) {
		
		for (ProductCustField productCustField : product.getProductCustFields()) {
			CustField custField = productCustField.getCustField();
			String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
			
			if (ValidateUtils.isObjectNotEmpty(this.custFieldValues.get(id))) {
				productCustField.setValue(this.custFieldValues.get(id).toString());
			}
			else {
				productCustField.setValue("");
			}
		}
	}

	public void reset() {
		this.productVersion = null;
        if (ValidateUtils.isObjectNotEmpty(this.custFieldValues)) this.custFieldValues.clear();
        
        this.initProductGroup(null);
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
        List<Tag> existingTags = this.productVersion.getProduct().getProductTags();
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
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
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

			custFieldGroup.getChildren().addAll(ApplicationUtils.dynamicBuildCustFieldsComponent("productBean", "custFieldValues", custFields, sessionMgntBean.hasInsufficientRoles("role_designer,role_admin")));
		}
		return custFieldGroup;
	}
	
	public void setCustFieldGroup(HtmlPanelGroup custFieldGroup) {
		this.custFieldGroup = custFieldGroup;
	}

	public boolean getCustFieldAvailable() {
		List<CustField> list = custFieldService.getCustFieldByProduct();
		return ValidateUtils.isObjectNotEmpty(list) ? true : false;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
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

	public ProductVersion getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
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
	
	public void setSelectedDocument(ProductVersionDocument document) {
		this.selectedDocument = document;
	}
}