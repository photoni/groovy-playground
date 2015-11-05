package util;

/**
 * Created by filippo on 11/5/15.
 */
public class MathUtil {
    public static double nDecimal(double val, int digits){
        double newVal=Math.round(val*Math.pow(10,digits+1))/Math.pow(10,digits+1);
        return newVal;
    }
}
