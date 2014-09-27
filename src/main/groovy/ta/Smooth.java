package ta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * groovy-playground
 * Created by filippo on 27/09/14.
 */

public class Smooth {
    private static Logger log= LoggerFactory.getLogger(Smooth.class);

    /**
     * @param startIndex
     * @param endIndex
     * @param values
     * @param periods
     * @return
     */
    public static double[] wSmoothed1Iterator(int startIndex, int endIndex, double[] values,int periods) {
        assert endIndex<=values.length;
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-1-periods; i++) {
            log.debug("i: {} - startIndex: {}",i,startIndex+i);
            result[i] = wSmoothed1(startIndex+i, values, periods, (short) 0);
        }
        return result;

    }

   /*
   * Wilder's smoothing First Technique
   *
   * @param startIndex
   * @param values
   * @param N periods
   * @param cursor
   * @return
   */
    public static double wSmoothed1(int startIndex,double[] values, int N, short cursor) {
        int currentIndex=startIndex+cursor;
        log.debug("current index: {} - N: {} ",currentIndex,N);

        double wsSmoothedPrev = 0;

        // Current smoothed = [Prior smoothed - (Prior smoothed)/N + Current]
        double result = 0D;
        if (cursor<N && (currentIndex+N)<values.length) {
            /* head recursive */
            log.debug("cursor: {} - startIndex: {}",cursor,startIndex);
            wsSmoothedPrev = wSmoothed1(startIndex, values, N, ++cursor);
            result = wsSmoothedPrev - (wsSmoothedPrev/N) + values[currentIndex];
        } else {

            double sum = 0;
            for (int j = 0; j < N; j++) {
                log.debug(" final currentIndex: {} - j: {} - val: {}",currentIndex,j,values[currentIndex + j]);
                sum += values[currentIndex + j];
            }

            result = sum;

        }

        return result;

    }

    /*
    * Wilder's smoothing Second Technique
    * @param startIndex
    * @param endIndex
    * @param highs
    * @param lows
    * @param N
    * @return
    */
    public static double wSmoothed2(int startIndex, int endIndex, double[] values,int N,short cursor,boolean rev) {
        int currentIndex=startIndex+cursor;
        double smoothedDmPrev = 0;

        // Current smoothedDM = [(Prior smoothedDM x 13) + Current DM] / 14
        double result = 0D;
        if (cursor<N) {
            smoothedDmPrev = wSmoothed2(startIndex + 1, endIndex, values, N, ++cursor, rev);
            result = ((smoothedDmPrev*(N-1)) + values[currentIndex])/N;
        } else if (cursor==N) {
            double sum = 0;
            for (int j = 0; j < N; j++) {
                sum += values[currentIndex + j];
            }

            result = sum / N;

        } else {
            result = 0;
        }

        return result;

    }
}
