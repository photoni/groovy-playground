package playground;

import groovy.transform.CompileStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArraySumJ {
	Logger log= LoggerFactory.getLogger(this.getClass());
	/**
	 * Sum of two integer
	 * @param x
	 * @param y
	 * @return
	 */
    @CompileStatic
	public int twoArgs(int x,int y){
		return x+y;
	}
	/** 
	 * Multiple arguments 
	 * @param args
	 * @return
	 */
	public int nArgs(int... args){
		int sum=0;
		long start= System.nanoTime();
		for (int i : args) {
			sum+=i;
		}
		long end= System.nanoTime();
		log.debug("Total time nano intern: {}", end-start);
		return sum;
	}

}
