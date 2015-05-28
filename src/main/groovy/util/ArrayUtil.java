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

	}

	public static double[] reverse(double[] values) {
		double[] result = new double[values.length];

		for (int i = 0; i <values.length ; i++) {
			result[values.length-i-1]=values[i];
		}
		return result;

	}




}
