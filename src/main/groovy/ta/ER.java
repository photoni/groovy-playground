package ta;

public class ER {

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
