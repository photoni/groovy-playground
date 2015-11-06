package util

import groovy.util.logging.Slf4j
import org.junit.Test

@Slf4j
class MathUtilTest {
    @Test
    public void nDecimalTest(){
        log.debug("Result: {}",MathUtil.nDecimal(1.0589999,2))
        log.debug("Result: {}",MathUtil.nDecimal(0.1847,2))
        log.debug("Result: {}",MathUtil.nDecimal(1.7976931348623157E308D,2))

    }
}
