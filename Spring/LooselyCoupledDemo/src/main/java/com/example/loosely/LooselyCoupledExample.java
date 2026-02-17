package com.example.loosely;

import com.example.demo.developer.BackendDeveloper;
import com.example.demo.developer.EnergeticEmployee;
import com.example.demo.developer.FrontendDeveloper;

//@SpringBootApplication
public class LooselyCoupledExample {

	public static void main(String[] args) {
//		SpringApplication.run(Demo1Application.class, args);
		
		EnergeticEmployee rosh = new EnergeticEmployee();
		rosh.work(new BackendDeveloper());
		rosh.work(new FrontendDeveloper());
		
		
	}

}
