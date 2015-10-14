package util;

public class ArrayUtil {

    public static double[] slice(double[] values, int startIndex, int endIndex) {
        double[] result = new double[endIndex - startIndex];
        int i = 0;
        for (int j = 0; j < values.length; j++) {
            if (j >= startIndex && j < endIndex) {
                result[i++] = values[j];
            }
        }
        return result;

    }

    public static double[] reverse(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[values.length - i - 1] = values[i];
        }
        return result;

    }

    public static double[] reverse(long[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[values.length - i - 1] = values[i];
        }
        return result;

    }

    public static double[] reverse(short[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[values.length - i - 1] = values[i];
        }
        return result;

    }

    public static double[] reverse(int[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[values.length - i - 1] = values[i];
        }
        return result;

    }

    /**
     * Fills up the array of values from lastIndex to index. Values are created with the following
     * increment=(value-lastValue)/positions
     *
     * @param lastValue
     * @param lastIndex
     * @param value
     * @param index
     * @param values
     */
    public static void stroke(double lastValue, int lastIndex, double value, int index, double[] values) {
        values[index] = value;
        int positions = index - lastIndex;
        double increment = (value - lastValue) / positions;
        for (int i = lastIndex + 1; i < index; i++) {
            int multi = i - lastIndex;
            double val = (multi * increment) + lastValue;
            values[i] = val;
        }
    }

    public  static boolean isPivot(double val_2,double val_1,double val){
        return (val_1>val_2 && val_1>val) || (val_1<val_2 && val_1<val);
    }

    public  static boolean isUpperPivot(double val_2,double val_1,double val){
        return  (val_1>val_2 && val_1>val) ;
    }

    public  static boolean isLowerPivot(double val_2,double val_1,double val){
        return  (val_1<val_2 && val_1<val) ;
    }


}
