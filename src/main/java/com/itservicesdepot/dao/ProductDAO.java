/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.Product;

public interface ProductDAO {

	public List<Product> getProducts();

	public int addProduct(Product product);

	public int updateProduct(Product product);

	public int deleteProduct(Product product);

	public Product getProduct(int id);

	public Product getProduct(String id);
	
	public List<Product> searchByKeyword(String keyword) throws Exception;
}
