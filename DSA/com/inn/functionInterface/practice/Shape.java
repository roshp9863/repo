package com.inn.functionInterface.practice;


interface Shapes {
	void draw();
}

//class Rectangle implements Shapes {
//
//	@Override
//	public void draw() {
//		System.out.println("RECTANGLE DRAW");
//	}
//	
//}


//class Circle implements Shapes {
//
//	@Override
//	public void draw() {
//		System.out.println("CIRCLE DRAW");
//	}
//	
//}



public class Shape {
	public static void main(String[] args) {
//		Shapes r = new Rectangle();
//		r.draw();
//		Shapes c = new Circle();
//		c.draw();

		
		Shapes rectangle = () -> System.out.println("RECTANGLE DRAW");
//		rectangle.draw();
		Shapes circle = () -> {System.out.println("CIRCLE DRAW");};
//		circle.draw();
		
		print(rectangle);
		print(circle);
	}
	
	public static void print(Shapes shape) {
		shape.draw();
	}
}
