package helpers

import org.slf4j.Logger

/**
 * groovy-playground
 * Created by filippo on 26/10/14.
 */
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
}
