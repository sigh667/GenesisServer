package com.mokylin.bleach.test.core.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

public class JSONTest {

	static class Element {
		
		private int value1;
		private String value2;
		private boolean isTrue;
		
		public Element(int value1, String value2, boolean isTrue) {
			this.value1 = value1;
			this.value2 = value2;
			this.isTrue = isTrue;
		}
		
		public Element() {
			
		}

		public int getValue1() {
			return value1;
		}

		public void setValue1(int value1) {
			this.value1 = value1;
		}

		public String getValue2() {
			return value2;
		}

		public void setValue2(String value2) {
			this.value2 = value2;
		}

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

	}
	
	@Test
	public void test_list_serialization_and_deserialization() {
		List<Element> elements = Lists.newLinkedList();
		elements.add(new Element(1, "1", true));
		elements.add(new Element(2, "2", false));
		
		String jsonStr = JSON.toJSONString(elements);
		System.out.println(jsonStr);
		
		List<Element> deserializeElements = JSON.parseArray(jsonStr, Element.class);
		Element e1 = deserializeElements.get(0);
		assertThat(e1.value1, is(1));
		assertThat(e1.value2, is("1"));
		assertThat(e1.isTrue, is(true));
		Element e2 = deserializeElements.get(1);
		assertThat(e2.value1, is(2));
		assertThat(e2.value2, is("2"));
		assertThat(e2.isTrue, is(false));
	}
	
}
