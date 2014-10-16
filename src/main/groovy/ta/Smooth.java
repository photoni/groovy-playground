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
     * TODO HACK to fix the endIndex-1 problem. Check why
     * @param startIndex
     * @param endIndex
     * @param values
     * @param periods
     * @return
     */
    public static double[] wSmoothed1Iterator(int startIndex, int endIndex, double[] values,int periods) {
        assert endIndex<=values.length;
        endIndex-=1;
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-(periods-1); i++) {
            log.debug("Cycle: {} - startIndex: {}",i,startIndex+i);
            result[i] = wSmoothed1(startIndex+i,endIndex, values, periods, (short) 0);
        }
        return result;

    }

    /**
     * @param startIndex
     * @param endIndex
     * @param values
     * @param periods
     * @param smoothType
     * @return
     */
    public static double[] wSmoothedIterator(int startIndex, int endIndex, double[] values,int periods,int smoothType) {
        assert endIndex<=values.length;
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-(periods-1); i++) {
            log.debug("Cycle: {} - startIndex: {}",i,startIndex+i);

            switch (smoothType ) {
                case 1:
                    result[i] = wSmoothed1(startIndex+i,endIndex, values, periods, (short) 0);
                    break;
                case 2:
                    result[i] = wSmoothed2(startIndex+i,endIndex, values, periods, (short) 0);
                    break;
               default:
                    throw new UnsupportedOperationException("Unknown smooth type");
            }


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
    public static double wSmoothed1(int startIndex,int endIndex,double[] values, int N, short cursor) {
        int currentIndex=startIndex+cursor;
        log.debug("current index: {} - N: {} ",currentIndex,N);

        double wsSmoothedPrev = 0;

        // Current smoothed = [Prior smoothed - (Prior smoothed)/N + Current]
        double result = 0D;
        if (cursor<N && (currentIndex+N)<endIndex) {
            /* head recursive */
            log.debug("cursor: {} - startIndex: {}",cursor,startIndex);
            wsSmoothedPrev = wSmoothed1(startIndex,endIndex, values, N, ++cursor);
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
    public static double wSmoothed2(int startIndex, int endIndex, double[] values,int N,short cursor) {
        int currentIndex=startIndex+cursor;
        double smoothedDmPrev = 0;
        log.debug("current index: {} - N: {} ",currentIndex,N);

        // Current smoothed = [(Prior smoothed x 13) + Current ] / 14
        double result = 0D;
        if (cursor<N && (currentIndex+N)<endIndex) {
            log.debug("cursor: {} - startIndex: {}",cursor,startIndex);
            smoothedDmPrev = wSmoothed2(startIndex,endIndex, values, N, ++cursor);
            result = ((smoothedDmPrev*(N-1)) + values[currentIndex])/N;
        } else {
            double sum = 0;
            for (int j = 0; j < N; j++) {
                log.debug(" final currentIndex: {} - j: {} - val: {}",currentIndex,j,values[currentIndex + j]);
                sum += values[currentIndex + j];
            }

            result = sum / N;

        }

        return result;

    }
}
