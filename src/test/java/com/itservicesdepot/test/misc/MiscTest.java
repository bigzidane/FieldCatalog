package com.itservicesdepot.test.misc;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.model.BaseDocument;
import com.itservicesdepot.service.DocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class MiscTest {
	static Logger logger = Logger.getLogger(MiscTest.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Test
	public void main() {
		System.out.println("This is the main test of Misc");
		
		List<BaseDocument> documents = null;
		try {
			 documents = documentService.searchInContentByKeywork("Test");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		Assert.assertNotNull(documents);
	}
}