package playground

import groovy.swing.impl.DefaultAction;

class MathBasic {
	static void main(def args) {

		def mygreeting = "Hello World"
		println mygreeting
		def hw=new MathBasic()
		println hw.sum(2,2)
		println hw.multiply(3, 3)
	}

	int sum (int a, int b){
		return a+b;
	}
	
	int multiply (int a, int b){
		return a*b;
	}
}
