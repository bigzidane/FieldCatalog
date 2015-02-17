/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.search.errors.EmptyQueryException;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import com.itservicesdepot.constant.NavigationConstant;
import com.itservicesdepot.model.Documents;
import com.itservicesdepot.model.Fields;
import com.itservicesdepot.model.Products;
import com.itservicesdepot.model.Screens;
import com.itservicesdepot.model.SearchResult;
import com.itservicesdepot.model.SearchResults;
import com.itservicesdepot.service.DocumentService;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.utils.ValidateUtils;

@ManagedBean(name="searchBean")
@ViewScoped
public class SearchBean extends BaseBean {
	static Logger logger = Logger.getLogger(SearchBean.class);
	
	private static final long serialVersionUID = 8603394760297522175L;

	@ManagedProperty("#{dozerMapper}")
    private Mapper mapper;
	
	private String searchIn;
	private String searchFor;
	
	private SearchResults searchResultList = new SearchResults();
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{documentService}")
    private DocumentService documentService;
	
	@ManagedProperty("#{fileStorageService}")
    private FileStorageService fileStorageService;
	
	@Autowired
    private SessionMgntBean sessionMgntBean;
	
	private String paramId;
	
	private SearchResult selectedSearchResult;
	
	@PostConstruct
    public void init() {
		String searchFor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(searchFor)) {
			this.searchFor = searchFor;
			this.searchIn = "";
			this.search();
			
			//this.sessionMgntBean.setSearchFor("");
		}
	}
	
	public void search() {
		this.reset();
		
		try {
			if (this.searchIn.equalsIgnoreCase("product")) {
				Products productList = new Products(this.productService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(productList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("screen")) {
				Screens screenList = new Screens(this.screenService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(screenList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("field")) {
				Fields fieldList = new Fields(this.fieldService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(fieldList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("document")) {
				Documents documentList = new Documents(this.documentService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(documentList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("document content")) {
				Documents documentList = new Documents(this.documentService.searchInContentByKeywork(this.searchFor));
				this.searchResultList = mapper.map(documentList, SearchResults.class);
			}
			else {
				Products productList = new Products(this.productService.searchByKeyword(this.searchFor));
				SearchResults searchResultProductList = mapper.map(productList, SearchResults.class);
				
				Screens screenList = new Screens(this.screenService.searchByKeyword(this.searchFor));
				SearchResults searchResultScreenList = mapper.map(screenList, SearchResults.class);
				
				Fields fieldList = new Fields(this.fieldService.searchByKeyword(this.searchFor));
				SearchResults searchResultFeildList = mapper.map(fieldList, SearchResults.class);
				
				this.searchResultList.add(searchResultProductList);
				this.searchResultList.add(searchResultScreenList);
				this.searchResultList.add(searchResultFeildList);
			}
		}
		catch (Exception e) {
			if (e instanceof EmptyQueryException) {
				String message = e.getMessage();
				if (message.indexOf("HSEARCH000146") != -1) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", String.format("The '%s' keyword has no meaningfull to be searched. Please validate the keyword and try again.", this.searchFor)));
				}
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The search enginee is facing an error. Please contact System Administrator.")));
			}
			
			logger.error(e.getMessage(),e);
		}
	}

	public String globalSearch() {
		this.setParamId(this.searchFor);
		return "successToSearchPage";
	}
	
	private void reset() {
		this.searchResultList = new SearchResults();
	}
	
	/**
	 * 
	 * @param searchResult
	 * @param mode	
	 * 			0	:	go to View/Edit page
	 * 			1	:	go to Screen Image page
	 * @return	
	 */
	public String showDetail(SearchResult searchResult, String mode) {
		this.setParamId(String.valueOf(searchResult.getId()));
		
		if (mode.equals("0") && searchResult.getType().equalsIgnoreCase("Screen")) {
			return NavigationConstant.NAV_TO_SCREEN_DETAIL;
		}
		else if (mode.equals("0") && searchResult.getType().equalsIgnoreCase("Field")) {
			return NavigationConstant.NAV_TO_FIELD_DETAIL;
		}
		if (mode.equals("0") && searchResult.getType().equalsIgnoreCase("Product")) {
			return NavigationConstant.NAV_TO_PRODUCT_DETAIL;
		}
		else if (mode.equals("1")) {
			return NavigationConstant.NAV_TO_IMAGE;
		}
		else {
			return "";
		}
	}
	
	public StreamedContent getFileDownload() {
		try {
			StreamedContent file = this.fileStorageService.getStreamedContent(this.selectedSearchResult.getExtraInfo(), this.selectedSearchResult.getName());
	        
	        return file;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' file is NOT found. Please contact System Administrator.",this.selectedSearchResult.getName())));
			logger.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public String getSearchIn() {
		return searchIn;
	}

	public void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}

	public String getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}

	

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public SearchResults getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(SearchResults searchResultList) {
		this.searchResultList = searchResultList;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public SessionMgntBean getSessionMgntBean() {
		return sessionMgntBean;
	}

	public void setSessionMgntBean(SessionMgntBean sessionMgntBean) {
		this.sessionMgntBean = sessionMgntBean;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public DocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public SearchResult getSelectedSearchResult() {
		return selectedSearchResult;
	}

	public void setSelectedSearchResult(SearchResult selectedSearchResult) {
		this.selectedSearchResult = selectedSearchResult;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}
}
