package com.itservicesdepot.test.product;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.ProductVersion;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.service.ProductService;
import com.itservicesdepot.service.VersionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class ProductTest {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private VersionService versionService;

	@Test
	public void main() {
		System.out.println("This is the main test of Product");
		
		ProductVersion newProductVersion = new ProductVersion();
		newProductVersion.setCloneFromVersionId(33);
		newProductVersion.setName("12.00.002");
		newProductVersion.setDescription("12.00.002");
		
		Product product = new Product();
		product.setId(106);
		newProductVersion.setProduct(product);
		
		Result result = null;
		try {
			result = this.versionService.addProductVersion(newProductVersion, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(result.getCode(), ErrorCodeConstant.SUCCESS);
		
	}
}