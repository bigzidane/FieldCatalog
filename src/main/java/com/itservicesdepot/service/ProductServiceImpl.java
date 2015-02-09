/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.dao.ProductDAO;
import com.itservicesdepot.dao.VersionDAO;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.Result;

@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDAO productDAO;
    
    @Autowired
    VersionDAO versionDAO;
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public Result addProduct(Product product) {
    	Result result = new Result();

    	result.setId(productDAO.addProduct(product));
    	
    	return result;
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }
    
    /*** Annotation of applying method level Spring Security ***/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = false)
    public int deleteProduct(Product product) {
        return productDAO.deleteProduct(product);
    }

    public List<Product> searchByKeyword(String keyword) throws Exception {
    	return productDAO.searchByKeyword(keyword);
    }
    
	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public List<Product> getProducts() {
    	return productDAO.getProducts();
    }
    
    public Product getProduct(int id) {
        Product product = productDAO.getProduct(id);
        return product;
    }
    
    public Product getProduct(String name) {
        Product product = productDAO.getProduct(name);
        return product;
    }

	public VersionDAO getVersionDAO() {
		return versionDAO;
	}

	public void setVersionDAO(VersionDAO versionDAO) {
		this.versionDAO = versionDAO;
	}
}
