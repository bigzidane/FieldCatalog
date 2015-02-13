package com.itservicesdepot.test.screen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.service.ScreenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class ScreenTest {

	@Autowired
	private ScreenService screenService;

	@Test
	public void main() {
		System.out.println("This is the main test of Screen");
	}
}