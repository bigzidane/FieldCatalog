/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="EVENT")
public class Event extends BaseModel{
	private static final long serialVersionUID = -2214839084569887055L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.event")
	private List<ScreenEvent> screenEvents = new ArrayList<ScreenEvent>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.event")
	private List<FieldEvent> fieldEvents = new ArrayList<FieldEvent>();

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "TARGET", nullable=false)
	private String target;
	
	@Column(name = "NAME", nullable=false)
	private String name;

	@Column(name = "EVENT_ORDER", nullable=false)
	private String order;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<ScreenEvent> getScreenEvents() {
		return screenEvents;
	}

	public void setScreenEvents(List<ScreenEvent> screenEvents) {
		this.screenEvents = screenEvents;
	}

	public List<FieldEvent> getFieldEvents() {
		return fieldEvents;
	}

	public void setFieldEvents(List<FieldEvent> fieldEvents) {
		this.fieldEvents = fieldEvents;
	}
}