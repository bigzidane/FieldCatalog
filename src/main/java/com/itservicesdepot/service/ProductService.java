/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.Result;

public interface ProductService {

	public List<Product> getProducts();

	public Result addProduct(Product product);

	public int updateProduct(Product product);

	public int deleteProduct(Product product);

	public Product getProduct(int id);

	public Product getProduct(String name);
	
	public List<Product> searchByKeyword(String keyword) throws Exception;
}
