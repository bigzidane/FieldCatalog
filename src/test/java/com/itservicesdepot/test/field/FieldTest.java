package com.itservicesdepot.test.field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.service.FieldService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class FieldTest {

	@Autowired
	private FieldService fieldService;

	@Test
	public void main() {
		System.out.println("This is the main test of Filed");
	}
}