package util;

import static org.junit.Assert.*;
import groovy.util.logging.Slf4j;

import org.junit.Test;

@Slf4j
class DateUtilTest {

	@Test
	public void test() {
		log.debug("date: {}",DateUtil.parseAsLong("2005-11-11"));
	}

}
