package com.mokylin.bleach.test.core.function;

import org.junit.Assert;
import org.junit.Test;

import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.function.exception.NotAFunctionException;
import com.mokylin.bleach.test.core.function.mock.MockClass1;
import com.mokylin.bleach.test.core.function.mock.MockClass2;

public class FunctionUtilTest {

	@Test
	public void final_primitive_or_string_or_log4j_field_should_be_allowed_by_assertIsFunctionClass_method() {
		try{
			FunctionUtil.assertIsFunctionClass(MockClass1.class);
			Assert.assertTrue(true);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail("final primitive and string failed!");
		}
		
	}
	
	@Test(expected = NotAFunctionException.class)
	public void class_with_field_should_not_allowed_by_assertIsFunctionClass_method(){
		FunctionUtil.assertIsFunctionClass(MockClass2.class);
		Assert.fail("Should not be here");
	}

}
