package playground;

public class ArraySumJ {
	/**
	 * Sum of two integer
	 * @param x
	 * @param y
	 * @return
	 */
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
		for (int i : args) {
			sum+=i;
		}
		return sum;
	}

}
