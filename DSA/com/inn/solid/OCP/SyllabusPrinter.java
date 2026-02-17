package com.inn.solid.OCP;

public class SyllabusPrinter {

	public void print(Course course) {
		if(course instanceof Java) {
			String java = ((Java) course).getJavaCourseContent();
			System.out.println(java);
		}
		if(course instanceof SpringBoot) {
			String spring = ((SpringBoot) course).getSpringCourseContent();
			System.out.println(spring);
		}
	}
}
