package ta;

import util.ArrayUtil;

/**
 * Rate-of-Change indicator is momentum in its purest form. It measures the percentage increase or decrease in price
 * over a given period of time. Think of its as the rise (price change) over the run (time). In general, prices are
 * rising as long as the Rate-of-Change remains positive. Conversely, prices are falling when the Rate-of-Change is
 * negative.
 * 21 days - short term trends
 * 63 - 125 days - mid term trends
 * 250 days long term trends -  very useful to track long running trends
 * <p/>
 * groovy-playground Created by filippo on 8/25/15.
 */
public class ROC {

    /**
     * RATE OF CHANGE
     */

    /**
     * @param values  the array of values ordered from newer to older
     * @param periods number of period to compute
     * @return Rate of change
     */
    public static double[] roc(int startIndex, int endIndex, double[] values,
                               int periods) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = rocFormula(startIndex + i, endIndex, values, periods);
        }

        return result;
    }

    /**
     * @param values  the array of values ordered from newer to older
     * @param periods number of period to compute
     * @return Rate of change
     */
    public static double[] roc(double[] values,
                               int periods) {
        int length = values.length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = rocFormula(i, length, values, periods);
        }

        return result;
    }

    /**
     * Create a composite indicator based on the list of ROC indicators in input. The composite indicator is the sum
     * of input indicators
     * @param rocs list of Roc histories computed with different periods. They must appear in the array from the
     *             fastest to the slowest reactive
     * @return
     */
    public static double[] composite(double[]... rocs) {
        int historyLen=rocs[0].length;
        double[] result = new double[historyLen];
        int rocsLen=rocs.length;

        for (int i = 0; i < historyLen; i++) {
            int sum=0;
            for (int j = 0; j < rocsLen; j++) {
                sum+=rocs[j][i];
            }
            result[i]=sum;

        }

        return result;
    }

    /**
     * Create a signal from an indicator
     * @param composite
     * @param threshold
     * @return 1 if the indicator is greater than threshold, -1 if it's lower than indicator, unchanged if it's
     * between threshold boundaries
     */
    public static double[] compositeSignal(double[] composite,double threshold) {
        int historyLen=composite.length;
        double[] result = new double[historyLen];

        for (int i = composite.length-2; i >= 0; i--) {
            if(composite[i]>threshold)
                result[i]=1;
            else if(composite[i]<threshold&&composite[i]>-threshold)
                result[i]=result[i+1];
            else
                result[i]=-1;
        }

        return result;
    }

    /*
    public static double[] compositeSignal(double[] prices, int roc1Period, int roc2Period, int roc3Period, int
            roc4Period, int
            roc5Period, int rocCompositeSmoothPeriod, int rocCompositeThreshold) {
        double[] roc1 = ROC.roc(prices, roc1Period);
        double[] roc2 = ROC.roc(prices, roc2Period);
        double[] roc3 = ROC.roc(prices, roc3Period);
        double[] roc4 = ROC.roc(prices, roc4Period);
        double[] roc5 = ROC.roc(prices, roc5Period);
        double[] rocComposite = composite(roc1, roc2, roc3, roc4, roc5);

        double[] rocCompositeSmooth = ArrayUtil.reverse(MA.sma(0, rocComposite.length, ArrayUtil
                .reverse(rocComposite), rocCompositeSmoothPeriod));

        double[] rocCompositeSignal = ROC.compositeSignal(rocCompositeSmooth, rocCompositeThreshold);

        return rocCompositeSignal;
    }*/

    public static double[] compositeSignal(int rocCompositeSmoothPeriod, int rocCompositeThreshold,double[] prices,Integer... rocPeriods) {


        return compositeSignal(1,rocCompositeSmoothPeriod,rocCompositeThreshold,prices,rocPeriods);
    }

    public static double[] compositeSignal(double sd,int rocCompositeSmoothPeriod, int rocCompositeThreshold,double[]
            prices,Integer... rocPeriods) {
        double[][] rocs=new double[rocPeriods.length][];
        for (int i = 0; i < rocPeriods.length; i++) {
            rocs[i]=ROC.roc(prices, rocPeriods[i]);
        }

        double[] rocComposite = composite(rocs);

        double[] rocCompositeSmooth = ArrayUtil.reverse(MA.sma(0, rocComposite.length, ArrayUtil
                .reverse(rocComposite), (int)Math.round(rocCompositeSmoothPeriod*sd)));

        double[] rocCompositeSignal = ROC.compositeSignal(rocCompositeSmooth, rocCompositeThreshold*sd);

        return rocCompositeSignal;
    }


    public static double rocFormula(int startIndex, int endIndex,
                                    double[] values, int periods) {
        double result = 0;

        int prevCloseIndex = startIndex + periods;
        if (prevCloseIndex < endIndex)
            result = ((values[startIndex] - values[prevCloseIndex]) / values[prevCloseIndex]) * 100;

        return result;
    }
}
