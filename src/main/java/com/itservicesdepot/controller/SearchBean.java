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
import org.springframework.beans.factory.annotation.Autowired;

import com.itservicesdepot.model.Fields;
import com.itservicesdepot.model.Products;
import com.itservicesdepot.model.Screens;
import com.itservicesdepot.model.SearchResult;
import com.itservicesdepot.model.SearchResults;
import com.itservicesdepot.service.FieldService;
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
	
	private Products productList = new Products();
	private Screens screenList = new Screens();
	private Fields fieldList = new Fields();
	private SearchResults searchResultList = new SearchResults();
	
	@ManagedProperty("#{productService}")
    private ProductService productService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@Autowired
    private SessionMgntBean sessionMgntBean;
	
	private String paramId;
	
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
				this.productList.setProducts(this.productService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(this.productList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("screen")) {
				this.screenList.setScreens(this.screenService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(this.screenList, SearchResults.class);
			}
			else if (this.searchIn.equalsIgnoreCase("field")) {
				this.fieldList.setFields(this.fieldService.searchByKeyword(this.searchFor));
				this.searchResultList = mapper.map(this.fieldList, SearchResults.class);
			}
			else {
				this.productList.setProducts(this.productService.searchByKeyword(this.searchFor));
				SearchResults searchResultProductList = mapper.map(this.productList, SearchResults.class);
				
				this.screenList.setScreens(this.screenService.searchByKeyword(this.searchFor));
				SearchResults searchResultScreenList = mapper.map(this.screenList, SearchResults.class);
				
				this.fieldList.setFields(this.fieldService.searchByKeyword(this.searchFor));
				SearchResults searchResultFeildList = mapper.map(this.fieldList, SearchResults.class);
				
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
			return "successToViewScreenPage";
		}
		else if (mode.equals("0") && searchResult.getType().equalsIgnoreCase("Field")) {
			return "successToViewFieldPage";
		}
		else if (mode.equals("1")) {
			return "successToImagePage";
		}
		else {
			return "";
		}
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

	public Screens getScreens() {
		return screenList;
	}

	public void setScreens(Screens screens) {
		this.screenList = screens;
	}

	public Screens getScreenList() {
		return screenList;
	}

	public void setScreenList(Screens screenList) {
		this.screenList = screenList;
	}

	public Fields getFieldList() {
		return fieldList;
	}

	public void setFieldList(Fields fieldList) {
		this.fieldList = fieldList;
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

	public Products getProductList() {
		return productList;
	}

	public void setProductList(Products productList) {
		this.productList = productList;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
