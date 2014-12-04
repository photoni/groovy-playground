package helpers

import groovy.util.logging.Slf4j
import org.slf4j.Logger

/**
 * groovy-playground
 * Created by filippo on 26/10/14.
 */
@Slf4j
class ArrayHelper {

    def static log(double[] array,Logger log,boolean withIndex){
        if(withIndex)
            array.eachWithIndex { def entry, int i ->
                log.debug('Index: {} - Value: {}',i,entry)
            }
        else
            array.each { def entry ->
                log.debug('Value: {}',i,entry)
            }

    }

    public static double[] closureIterator(int startIndex, double[] values,int periods,Closure formula) {
        def endIndex=values.length;
        int length = endIndex - startIndex;
        double[] result = new double[length];
        log.debug("Periods: {}",periods);
        for (int i = 0; i < length-(periods-1); i++) {
            def startIndexOffset = startIndex + i
            log.debug("-------->>>>>>>>Cycle: {} - startIndex: {}  - value: {}",i, startIndexOffset,values[startIndexOffset]);
            result[i]=formula.call(startIndexOffset,values,periods)
        }
        return result;

    }


}
