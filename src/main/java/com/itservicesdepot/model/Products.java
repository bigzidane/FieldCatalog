package com.itservicesdepot.model;

import java.util.List;

public class Products extends BaseModel {
	private static final long serialVersionUID = -6143455938208325391L;

	private List<Product> products;

	public Products() {
		
	}
	
	public Products(List<Product> products) {
		this.products = products;
	}
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
