package de.odt.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import weka.classifiers.trees.HoeffdingTree;

public class Test {

	@org.junit.Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		
		HoeffdingTree ht = context.getBean(HoeffdingTree.class);
		
		
	}

}
