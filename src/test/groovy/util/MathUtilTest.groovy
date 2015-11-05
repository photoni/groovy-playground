package util

import groovy.util.logging.Slf4j
import org.junit.Test

@Slf4j
class MathUtilTest {
    @Test
    public void nDecimalTest(){
        log.debug("Result: {}",MathUtil.nDecimal(1.0599999,2))
    }
}
