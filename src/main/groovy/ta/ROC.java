package ta;

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


    public static double rocFormula(int startIndex, int endIndex,
                                    double[] values, int periods) {
        double result = 0;

        int prevCloseIndex = startIndex + periods;
        if (prevCloseIndex < endIndex)
            result = ((values[startIndex] - values[prevCloseIndex]) / values[prevCloseIndex]) * 100;

        return result;
    }
}
