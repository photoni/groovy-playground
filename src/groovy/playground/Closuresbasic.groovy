package groovy.playground

import groovy.lang.Closure;

class ClosuresBasic {
	static void main(def args) {


		def cb=new ClosuresBasic();

		println cb.arraySum(1,2,3,4,5,6,7,8,9,0)
		/*we use the function f1 as closure*/
		println cb.sumClosureIterator(cb.f1,1,2,3,4,5,6,7,8,9,0)
	}

	/**
	 * Definition of a closure. The type {@link Closure} can be omitted
	 * If we add the return type integer it doesn't work anymore. 
	 */
	def Closure f1={int a , int b -> a+=b };
	/**
	 * Variable argument closures
	 * @param args
	 * @return
	 */
	int arraySum(int... args){
		int sum=0
		args.each {i -> sum+=i}
		return sum
	}

	int sumClosureIterator(closure, int... args){
		int sum=0;
		args.each {i -> sum=closure(sum,i)}
		return sum
	}
}
