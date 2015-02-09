package com.itservicesdepot.model;

import java.util.List;

public class Fields extends BaseModel {
	private static final long serialVersionUID = 3976545039295890788L;
	
	private List<Field> fields;

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
