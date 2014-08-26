package util;

public class ArrayUtil {

	public static double[] slice(double[] values, int startIndex, int endIndex) {
		double[] result = new double[endIndex - startIndex];
		int i=0;
		for (int j = 0; j < values.length; j++) {
			if(j>=startIndex && j< endIndex){
				result[i++]=values[j];			
			}
		}
		return result;

	};

}
