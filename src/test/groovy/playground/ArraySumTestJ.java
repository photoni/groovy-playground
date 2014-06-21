package playground;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArraySumTestJ {
	Logger logger = LoggerFactory.getLogger(ArraySumTestJ.class);

	@Test
	public void arraySumTwoArgs() {
		ArraySumJ arraySum = new ArraySumJ();
		long start = System.nanoTime();
		int sum = arraySum.twoArgs(2, 2);
		long end = System.nanoTime();
		logger.debug("Total time nano : {}", end - start);
		assertEquals(4, sum);
	}

	@Test
	public void twoIntSum() {

		long start = System.nanoTime();
		int sum = 2 + 2;
		long end = System.nanoTime();
		logger.debug("Total time nano : {}", end - start);
		assertEquals(4, sum);

	}

	@Test
	public void arraySumMultiArgs() {
		ArraySumJ arraySum = new ArraySumJ();
		long start = System.nanoTime();
		int sum = arraySum.nArgs(2, 2, 3, 3, 2, 1, 2);
		long end = System.nanoTime();
		logger.debug("Total time nano : {}", end - start);
		assertEquals(15, sum);
	}

}
