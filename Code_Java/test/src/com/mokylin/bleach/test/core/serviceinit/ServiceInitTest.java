package com.mokylin.bleach.test.core.serviceinit;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mokylin.bleach.core.serviceinit.ServiceInitManager;
import com.mokylin.bleach.core.serviceinit.exception.CircularDependencyException;
import com.mokylin.bleach.core.serviceinit.exception.WrongServiceObjectFieldException;
import com.mokylin.bleach.test.core.serviceinit.service.Statistics;
import com.mokylin.bleach.test.core.serviceinit.servicedep.StatisticsDep;
import com.mokylin.bleach.test.core.serviceinit.servicefilter.Filter;
import com.mokylin.bleach.test.core.serviceinit.servicefilter.StatisticsFilter;

public class ServiceInitTest {
	
	@Test
	public void all_services_should_be_inited() {
		ServiceInitManager.initAllServices("com.mokylin.bleach.test.core.serviceinit.service");
		
		Assert.assertArrayEquals(new Boolean[] { true, true, true },
				new Boolean[] { Statistics.service1Inited, Statistics.service2Inited, Statistics.service3Inited });
	}
	
	@Test
	public void service4_should_be_the_last_to_be_inited() {
		ServiceInitManager.initAllServices("com.mokylin.bleach.test.core.serviceinit.servicedep");
		
		Assert.assertArrayEquals(new int[] { 3 }, new int[] { StatisticsDep.service4InitOrder });
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void should_throw_circular_reference_exception() {
		thrown.expect(CircularDependencyException.class);
		
		ServiceInitManager.initAllServices("com.mokylin.bleach.test.core.serviceinit.servicecirculardep");
	}
	
	@Test
	public void should_throw_service_object_field_exception() {
		thrown.expect(WrongServiceObjectFieldException.class);
		thrown.expectMessage("Service class <com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield.Service10> don't have static "
				+ "<com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield.Service10> field!");
		
		ServiceInitManager.initAllServices("com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield");
	}
	
	@Test
	public void all_services_should_be_inited_with_filter() {
		ServiceInitManager.initAllServices("com.mokylin.bleach.test.core.serviceinit.servicefilter", Filter.class);
		
		Assert.assertArrayEquals(new Boolean[] { true, true, true, false, false },
				new Boolean[] { StatisticsFilter.service12Inited, StatisticsFilter.service13Inited, StatisticsFilter.service14Inited, StatisticsFilter.service15Inited, StatisticsFilter.service16Inited });
	}
}
