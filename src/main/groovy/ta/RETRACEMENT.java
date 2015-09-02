package ta;

/**
 * created by filippo on 9/2/15.
 */
public class RETRACEMENT {
    /**
     *
     * @param values
     * @param minimumReatracementRate price movements under this rate are ignored
     * @return the zigzag for the given rate and fibonacci levels
     */
    public double[][] fibonacci(double[] values,int minimumReatracementRate){
        double[][] result=new double[2][values.length];
        double[] zigzag=ZIGZAG.zigZag(values,minimumReatracementRate);

        return result;
    }
}
