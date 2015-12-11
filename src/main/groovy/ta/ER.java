package ta;

/**
 * Efficient Ratio(ER) indicator. The ER is basically the price change adjusted for the daily volatility.<br/> ER
 * measures the efficiency of the current movement of a curve.ER would be 1 if prices moved up 10 consecutive periods or
 * down 10 consecutive periods
 */
public class ER {

    /**
     * Computes ER
     *
     * @param values
     * @param periods the number of periods to look back
     * @return A number between 0 and 1. The greater the number the bigger the efficiency
     */
    public static double[] er(double[] values, int periods) {
        final int length = values.length;
        double[] result = new double[length];
        double[] change = change(values, periods);
        double[] volatility = volatility(values, periods);
        for (int i = 0; i < length; i++) {
            if (volatility[i] > 0)
                result[i] = change[i] / volatility[i];
        }
        return result;
    }

    /**
     * Adaptive smoothing constant
     *
     * @param values
     * @param periods
     * @param fastest the fastest constant (suggested 2)
     * @param slowest the slowest constant (suggested 30)
     * @return
     */
    public static double[] sc(double[] values, int periods, int fastest, int slowest) {
        final int length = values.length;
        double[] result = new double[length];
        double[] er = er(values, periods);
        for (int i = 0; i < length; i++) {
            result[i] = scFormula(er[i], fastest, slowest);
        }
        return result;
    }

    public static double[] change(double[] values, int periods) {
        double[] result = new double[values.length];
        for (int i = periods; i < values.length; i++) {
            result[i] = baseFormula(values[i], values[i - periods]);
        }
        return result;
    }

    public static double[] volatility(double[] values, int periods) {
        double[] result = new double[values.length];

        for (int i = periods; i < values.length; i++) {
            result[i] = sumFormula(values, i - periods, i);
        }
        return result;
    }

    /**
     * @param er      efficiency ratio
     * @param fastest the fastest constant (suggested 2)
     * @param slowest the slowest constant (suggested 30)
     * @return
     */
    private static double scFormula(double er, int fastest, int slowest) {
        return Math.pow(((er * (2D / (fastest + 1) - 2D / (slowest + 1))) + 2D / (slowest + 1)), 2);
    }

    private static double baseFormula(double close, double priorClose) {
        return Math.abs(close - priorClose);
    }

    private static double sumFormula(double[] values, int from, int to) {
        double sum = 0;
        for (int i = from + 1; i <= to; i++) {
            sum += baseFormula(values[i], values[i - 1]);

        }
        return sum;
    }

}
