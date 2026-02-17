package com.inn.solid.OCP;

public class SyllabusApp {

	public static void main(String[] args) {
		SyllabusPrinter s = new SyllabusPrinter();
		s.print(new Java());
		s.print(new SpringBoot());
	}
}
