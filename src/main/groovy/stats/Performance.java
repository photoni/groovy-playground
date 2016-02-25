package stats;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.runtime.ArrayUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used for statistics about performance Created by filippo on 1/21/16.
 */
public class Performance {
    /**
     * @param from beginning value
     * @param to   ending value
     * @param perc if true the result is a percentage
     * @return
     */
    public static Double gain(double from, double to, boolean perc) {
        double gain = (to - from) / from;
        return perc ? gain * 100 : gain;
    }

    /**
     * @param signal 1 means invested 0 means not invested
     * @param values values against which compute the gain
     * @param perc if return percentage
     * @return
     */
    public static Double[] gainSignal(double[] signal, double[] values,boolean perc) {
        List<Double> gains = new LinkedList<Double>();
        boolean rec = false;
        double from=0;
        for (int i = 0; i < signal.length; i++) {

            if (signal[i] > 0) {
                if (rec == false)
                    from = values[i];
                rec = true;
            }else if(signal[i] < 0)
            {
                if (rec == true)
                    gains.add(gain(from,values[i],perc));
                rec=false;
                from=0;
            }


        }
        return util.ArrayUtil.toDoubleArray(gains);

    }


}
