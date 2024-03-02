package com.example.tightly;

import com.example.tightly.developer.BackendDeveloper;
import com.example.tightly.developer.EnergeticMan;

public class TightlyCoupledExample {

	public static void main(String[] args) {
        EnergeticMan Rex = new EnergeticMan();
        Rex.work(new BackendDeveloper());
		
	}

}
