package ta;

/**
 * The ZigZag feature on SharpCharts is not an indicator per se, but rather a means to filter out smaller price
 * movements. A ZigZag set at 10% would ignore all price movements less than 10%. Only price movements greater than 10%
 * would be shown
 * <p/>
 * groovy-playground Created by filippo on 8/26/15.
 */
public class ZIGZAG {

    /**
     * @param values arrays of values
     * @param rate price movements smaller that it are ignored
     **/
    static double[] zigZag(double[] values, int rate) {
        double[] result = new double[values.length];
        /* the return from the last confirmed pivot to the current value */
        double exploringReturn = 0;
        /* the index of the last confirmed pivot */
        int pivotCandidateIndex = 0;
         /* the value of the last confirmed pivot */
        double pivotCandidateVal = 0;
        /* the type of pivot: -1 marks a minimum and +1 marks a maximum */
        int pivotCandidateType = 0;
        for (int i = 1; i < values.length; i++) {
            exploringReturn = ((values[i] - pivotCandidateVal) / pivotCandidateVal);
            /* if the exploring return exceed the rate we have a new pivot candidate */
            if (Math.abs(exploringReturn) > rate) {
                /* the current pivot is saved int the result */
                result[pivotCandidateIndex] = pivotCandidateVal;
                /* we set the new pivot candidate to the current value */
                stroke(pivotCandidateVal,pivotCandidateIndex,values[i],i,values);
                pivotCandidateIndex = i;
                pivotCandidateVal = values[i];
                pivotCandidateType = (int) Math.signum(exploringReturn);
            } else if (((values[i] - pivotCandidateVal) * pivotCandidateType) > 0) {
                /* if the exploringReturn doesn't exceed the rate and the current value is a new min/max we have a new
                 candidate  */
                pivotCandidateIndex = i;
                pivotCandidateVal = values[i];
            }

        }
        return result;
    }

    private static void stroke(double lastValue,int lastIndex,double value, int index,double[] values) {
        int positions=index-lastIndex;
        double increment=(value-lastValue)/positions;
        for (int i = lastIndex+1; i < index ; i++) {
            int multi=i-lastIndex;
            double val=multi*lastValue;
            values[i]=val;
        }
    }

}
