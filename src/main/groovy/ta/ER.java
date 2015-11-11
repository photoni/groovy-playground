package ta;

/**
 * Efficient Ratio(ER) indicator. The ER is basically the price change adjusted for the daily volatility
 */
public class ER {

    /**
     * Computes ER
     * @param values
     * @param periods
     * @return
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
     *
     * @param values
     * @param periods
     * @param fastest the fastest constant (suggested 2)
     * @param slowest the slowest constant (suggested 30)
     * @return
     */
    public static double[] sc(double[] values, int periods,int fastest,int slowest) {
        final int length = values.length;
        double[] result = new double[length];
        double[] er=er(values,periods);
        for (int i = 0; i < length; i++) {
            result[i]=scFormula(er[i],fastest,slowest);
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
     *
     * @param er efficiency ratio
     * @param fastest the fastest constant (suggested 2)
     * @param slowest the slowest constant (suggested 30)
     * @return
     */
    private static double scFormula(double er,int fastest, int slowest) {
        return Math.pow(((er * (2D/(fastest+1) - 2D/(slowest+1))) + 2D/(slowest+1)),2);
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
