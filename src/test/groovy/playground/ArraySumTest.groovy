package playground

import groovy.transform.CompileStatic;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import org.junit.Test
@Slf4j
@CompileStatic
class ArraySumTest {

	@Test
	void arraySumTwoArgs() {
		def arraySum=new ArraySum();
		long start= System.nanoTime();
		int sum= arraySum.twoArgs(2,2);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		assertEquals(4, sum);
	}

	@Test
	void arraySumClosureIterator(){
		def arraySum=new ArraySum();
		/*we use the function f1 as closure*/
		long start= System.nanoTime();
		int sum= arraySum.sumClosureIterator(arraySum.f1,1,2,3,4,5,6,7,8,9,0);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end - start);
		assertEquals(45, sum);
	}

	@Test
	public void twoIntSum() {
		long start= System.nanoTime();
		int sum =2+2;
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end - start);
		assertEquals(4, sum);
	}

	@Test
	public void twoIntSumAdjusted() {
		long start= System.nanoTime();
		Integer two=new Integer(2)
		int sum =two+two;
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end - start);

		start= System.nanoTime();
		two=new Integer(2)
		sum =two+two;
		end= System.nanoTime();
		log.debug("Total time nano : {}", end - start);
		assertEquals(4, sum);

		start= System.nanoTime();
		two=new Integer(2)
		sum =two+two;
		end= System.nanoTime();
		log.debug("Total time nano : {}", end - start);
		assertEquals(4, sum);
	}

	@Test
	void arraySumMultiArgs() {
		def arraySum=new ArraySumJ();
		long start= System.nanoTime();
		int sum= arraySum.nArgs(2,2,3,3,2,1,2);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		assertEquals(15, sum);
	}
}
