package com.itservicesdepot.model;

import java.util.List;

public class Documents extends BaseModel {
	private static final long serialVersionUID = 646584731207471765L;

	private List<BaseDocument> documents;

	public List<BaseDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<BaseDocument> documents) {
		this.documents = documents;
	}

}
