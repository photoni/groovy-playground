package util;

/**
 *
 * Created by filippo on 11/5/15.
 */
public class MathUtil {
    /**
     * The following condition must be true <code>Math.abs(Double.MAX_VALUE / Math.pow(10, digits)) > Math.abs(val)</code>
     * in order to use this method
     * @param val
     * @param digits the number decimal digits
     * @return
     */
    public static double nDecimal(double val, int digits) {
        if (Math.abs(Double.MAX_VALUE / Math.pow(10, digits)) > Math.abs(val))
            return  Math.round(val * Math.pow(10, digits)) / Math.pow(10, digits);
        return val;
    }
}
