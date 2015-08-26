package ta;

/**
 * The ZigZag feature on SharpCharts is not an indicator per se, but rather a means to filter out smaller price
 * movements. A ZigZag set at 10% would ignore all price movements less than 10%. Only price movements greater than 10%
 * would be shown
 * <p/>
 * groovy-playground Created by filippo on 8/26/15.
 */
public class ZIGZAG {

    double[] zigZag(double[] values, int rate){
        double [] result= new double[values.length];
        double ll=Double.MAX_VALUE;
        double hh=Double.MIN_VALUE;
       

        for (int i = 2; i < values.length; i++) {
            int lastMoveDirection=(int)Math.signum(values[i]-values[i-1]);
            int prevMoveDirection=(int)Math.signum(values[i-1]-values[i-2]);


        }
        return null;
    }

}
