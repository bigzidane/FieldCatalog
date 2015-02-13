package com.itservicesdepot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.itservicesdepot.test.field.FieldTest;
import com.itservicesdepot.test.misc.MiscTest;
import com.itservicesdepot.test.product.ProductTest;
import com.itservicesdepot.test.screen.ScreenTest;
import com.itservicesdepot.test.system.SystemTest;

@RunWith(Suite.class)
@SuiteClasses({ ProductTest.class, ScreenTest.class, FieldTest.class, SystemTest.class, MiscTest.class })
public class MainTest {
	
	@Test
	public void main() {
		System.out.println("This is the main test of FieldCatalog.");
	}
	
}