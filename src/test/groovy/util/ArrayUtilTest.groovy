package util;

import static org.junit.Assert.*;
import groovy.util.logging.Slf4j;

import org.junit.Test;
@Slf4j
class ArrayUtilTest {

	@Test
	public void test() {
		def values=[1, 2, 3, 4, 5, 6, 7, 8, 9] as double[]
		def sliced=ArrayUtil.slice(values, 2, 4)
		log.debug("sliced: {}",sliced);
	}
}
