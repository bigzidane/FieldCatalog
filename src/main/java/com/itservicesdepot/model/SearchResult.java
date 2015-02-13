/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SearchResult implements Serializable, Comparable<SearchResult> {
	private static final long serialVersionUID = -4678663438097973182L;
	
	private int id;
	private String name;
	private String type;
	private String description;
	private int count;
	private String extraInfo;
	
	public SearchResult() {
	}
	
	public SearchResult(int id, String name, String type, String description) {
		this.id = id;
		this.name = name;
        this.type = type;
        this.description = description;
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(SearchResult arg0) {
		 return this.getName().compareTo(arg0.getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        SearchResult other = (SearchResult) obj;
        if (this.getId() == other.getId() && this.getName().equals(other.getName())) return true;
        
        return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(51, 79).   
			      append(id).append(name).
			      toHashCode();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

}
