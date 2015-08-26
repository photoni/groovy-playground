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
        double hh=Double.MIN_VALUE;
        double ll=Double.MAX_VALUE;

        for (int i = 0; i < values.length; i++) {
            hh=Math.max(hh,values[i]);
            ll=Math.min(ll, values[i]);
            if(Math.abs((hh-ll)/ll)>rate)
                result[i]=values[i];

        }
        return null;
    }

}
