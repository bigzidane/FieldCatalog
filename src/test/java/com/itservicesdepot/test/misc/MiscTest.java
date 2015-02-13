package com.itservicesdepot.test.misc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class MiscTest {

	@Test
	public void main() {
		System.out.println("This is the main test of Misc");
	}
}