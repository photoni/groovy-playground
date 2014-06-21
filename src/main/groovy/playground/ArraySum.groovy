package playground

import groovy.lang.Closure;

/**
 * This class contains a couple of examples about signature syntax on array parameters 
 * @author filippo
 *
 */
class ArraySum {

	/**
	 * two arguments
	 * @param x
	 * @param y
	 * @return
	 */
	int twoArgs(int x,int y){
		return x+y;
	}
	/**
	 * Definition of a closure. The type {@link Closure} can be omitted.
	 *
	 */
	def Closure f1={int a , int b -> a+=b };
	/**
	 * We define a sum function with a closure
	 * @param closure
	 * @param args
	 * @return
	 */
	int sumClosureIterator(closure, int... args){
		int sum=0;
		args.each {i -> sum=closure(sum,i)}
		return sum
	}

	/** 
	 * Multiple arguments 
	 * @param args
	 * @return
	 */
	int nArgs(int... args){
		int sum=0
		args.each {i -> sum+=i}
		return sum
	}
}
