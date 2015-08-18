package ta;

import org.apache.commons.lang.math.NumberUtils;

/**
 * groovy-playground
 * Created by filippo on 29/10/14.
 */
public class RSI {
    public static double[] averageGain(double[] values,int periods){
        double[][] deltas =MathAnalysis.deltas(values);
        double[] gains=deltas[0];

        return Smooth.wSmoothedIterator(0,gains.length,gains,periods,2);
    }

    public static double[] averageLoss(double[] values,int periods){
        double[][] deltas =MathAnalysis.deltas(values);
        double[] losses=deltas[1];

        return Smooth.wSmoothedIterator(0,losses.length,losses,periods,2);
    }

    public static double[] rs(double[] values,int periods){
        double[] averageLoss=averageLoss(values,periods);
        double[] averageGain=averageGain(values, periods);
        double[] rs=new double[averageGain.length];

        for (int i = 0; i < averageGain.length; i++) {
            rs[i]=averageGain[i]/averageLoss[i];
        }
        return rs;
    }

    public static double[] rsi(double[] values,int periods){
        double[] rs=rs(values,periods);
        double[] rsi=new double[rs.length];

        for (int i = 0; i < rs.length; i++) {
            rsi[i]=100 - (100/(1+rs[i]));
            if(Double.isNaN(rs[i]))
                rsi[i]=0;
        }
        return rsi;
    }

}
