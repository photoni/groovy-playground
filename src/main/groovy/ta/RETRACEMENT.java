package ta;

import util.ArrayUtil;

/**
 * created by filippo on 9/2/15.
 */
public class RETRACEMENT {
    private double level38=38.2;
    private double level50=50;
    private double level61=61.8;
    /**
     *
     * @param values
     * @param minimumReatracementRate price movements under this rate are ignored
     * @return the zigzag for the given rate and fibonacci levels
     */
    public double[] fibonacci(double[] values,int minimumReatracementRate){

        double[] result=new double[values.length];
        double[] zigzag=ZIGZAG.zigZag(values,minimumReatracementRate);
        int trendStartIndex=0;
        double  trendStartVal=0;
        for (int i = 2; i < zigzag.length ; i++) {
            double val_2=zigzag[i-2];
            double val_1=zigzag[i-1];
            double val=zigzag[i];
            if(ArrayUtil.isPivot(val_2,val_1,val)){
                trendStartVal=val_1;
                trendStartIndex=i-1;
            }
            double rate=(val-trendStartVal)/trendStartVal;
            double rate_1=(val_1-trendStartVal)/trendStartVal;
            if(isCrossinLevels(rate_1,rate))
                result[i-1]=val_1;

        }
        return result;
    }

    private boolean isCrossinLevels(double rate_1,double rate){
        return isCrossingLevel(rate_1,rate,level38) || isCrossingLevel(rate_1,rate,level50) || isCrossingLevel
                (rate_1,rate,level61);
    }

    private boolean isCrossingLevel(double rate_1,double rate,double level){
        return ((rate_1<level  &&  level<rate)|| (rate_1>level  &&  level>rate));
    }


}
