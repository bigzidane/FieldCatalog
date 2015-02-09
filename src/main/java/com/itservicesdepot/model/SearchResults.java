package com.itservicesdepot.model;

import java.util.ArrayList;
import java.util.List;

import com.itservicesdepot.utils.ValidateUtils;

public class SearchResults extends BaseModel {
	private static final long serialVersionUID = -4436384022789875524L;

	private List<SearchResult> searchResults;

	public List<SearchResult> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<SearchResult> searchResults) {
		this.searchResults = searchResults;
	}
	
	public void add(SearchResults searchResult) {
		if (ValidateUtils.isObjectNotEmpty(searchResult) && ValidateUtils.isObjectNotEmpty(searchResult.getSearchResults())) {
			if (ValidateUtils.isObjectEmpty(this.searchResults)) this.searchResults = new ArrayList<SearchResult>();
			
			this.searchResults.addAll(searchResult.getSearchResults());
		}
	}

}
