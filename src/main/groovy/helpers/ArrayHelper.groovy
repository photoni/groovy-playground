package helpers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.slf4j.Logger

/**
 * groovy-playground
 * Created by filippo on 26/10/14.
 */
@Slf4j
class ArrayHelper {

    def static log(def array,Logger log,boolean withIndex){
        _log(withIndex, array, log, '')

    }
    def static log(def array,Logger log,boolean withIndex,String level){
        _log(withIndex, array, log, level)

    }

    def static logCompare(def array1,def array2,Logger log){
        _logCompare(array1, array2, log, '')

    }
    def static logCompare(def array1,def array2,def array3,Logger log){
        _logCompare(array1, array2,array3, log, '')

    }

    private static _log(boolean withIndex, array, log, String level) {
        def upperCase = level.toUpperCase()
        if (withIndex)
            array.eachWithIndex { def entry, int i ->
                switch (upperCase){
                    case 'WARN':
                        log.debug('Index: {} - Value: {}', i, entry)
                        break;
                    case 'TRACE':
                        log.trace('Index: {} - Value: {}', i, entry)
                        break;
                    default:
                        log.debug('Index: {} - Value: {}', i, entry)

                }

            }
        else
            array.each { def entry ->
                switch (upperCase){
                    case 'WARN':
                        log.debug('Value: {}', entry)
                        break;
                    case 'TRACE':
                        log.trace('Value: {}', entry)
                        break;
                    default:
                        log.debug('Value: {}', entry)

                }

            }
    }

    private static _logCompare(array1,array2, log, String level) {
        def upperCase = level.toUpperCase()

            array1.eachWithIndex { def entry, int i ->
                switch (upperCase){
                    case 'WARN':
                        log.debug('Index: {} - Value1: {} - Value2: {}', i, entry,array2[i])
                        break;
                    case 'TRACE':
                        log.trace('Index: {} - Value1: {} - Value2: {}', i, entry,array2[i])
                        break;
                    default:
                        log.debug('Index: {} - Value1: {} - Value2: {}', i, entry,array2[i])

                }

            }

    }

    private static _logCompare(array1,array2,array3, log, String level) {
        def upperCase = level.toUpperCase()

        array1.eachWithIndex { def entry, int i ->
            switch (upperCase){
                case 'WARN':
                    log.debug('Index: {} - Value1: {} - Value2: {} - Value3: {}', i, entry,array2[i],array3[i])
                    break;
                case 'TRACE':
                    log.trace('Index: {} - Value1: {} - Value2: {} - Value3: {}', i, entry,array2[i],array3[i])
                    break;
                default:
                    log.debug('Index: {} - Value1: {} - Value2: {} - Value3: {}', i, entry,array2[i],array3[i])

            }

        }

    }

    /**
     * Iterator over an array of values. It calls the closure on the array of values at each loop
     * @param startIndex
     * @param values
     * @param periods
     * @param formula
     * @return
     */
    @CompileStatic
    public static double[] closureIterator(int startIndex, double[] values,int periods,Closure formula) {
        def endIndex=values.length;
        int length = endIndex - startIndex;
        double[] result = new double[length];
        log.debug("Periods: {}",periods);
        for (int i = 0; i < length-(periods-1); i++) {
            int startIndexOffset = startIndex + i;
            log.debug("-------->>>>>>>>Cycle: {} - startIndex: {}  - value: {}",i, startIndexOffset,values[startIndexOffset]);
            result[i]=(Double)formula.call(startIndexOffset,values,periods)
        }
        return result;

    }

    /**
     * Pure iterator. It simply calls the closure at each loop
     * @param startIndex
     * @param endIndex
     * @param periods
     * @param formula The closure to be called(defined as ; {start,periods -> <your code here>})
     * @return
     */
    @CompileStatic
    public static double[] closureIterator(int startIndex, int endIndex,int periods,Closure formula) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length-(periods-1); i++) {
            def startIndexOffset = startIndex + i;
            result[i]=(Double)formula.call(startIndexOffset,periods)
        }
        return result;

    }

    /**
     * Pure iterator. It simply calls the closure at each loop
     * @param startIndex
     * @param endIndex
     * @param formula The closure to be called(defined as ; {index -> <your code here>})
     * @return
     */
    @CompileStatic
    public static double[] closureIterator(int startIndex, int endIndex,Closure formula) {
        int length = endIndex - startIndex;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            def startIndexOffset = startIndex + i;
            result[i]=(Double)formula.call(startIndexOffset)
        }
        return result;

    }


    /**
     * Iterator over an array of values. It calls the closure on the array of values at each loop
     * @param values
     * @param formula
     * @return
     */
    @CompileStatic
    public static double[] closureIterator(double[] values,Closure formula) {
        def endIndex=values.length;
        double[] result = new double[endIndex];

        for (int i = 0; i < endIndex; i++) {
            log.debug("-------->>>>>>>>Cycle: {} - startIndex: {}  - value: {}",i,values[i]);
            result[i]=(Double)formula.call(i,values[i])
        }
        return result;

    }





}
