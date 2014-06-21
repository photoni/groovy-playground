package playground;

import static org.junit.Assert.*

import groovy.util.logging.Slf4j

import org.junit.Test
@Slf4j
class HttpClientBasicTest {

	@Test
	void httpGetWithTest() {
		def hcb=new HttpClientBasic();
		String resp=hcb.getHttpSimple("http://google.com");
		long start= System.nanoTime();
		def json=hcb.getJson("http://api.openweathermap.org/data/2.5/",[ path: 'weather', query: [q: 'London']]);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		log.debug("It is currently ${json.weather.description} in London.");
	}

	@Test
	void httpGetCSV() {
		def hcb=new HttpClientBasic();
		long start= System.nanoTime();
		def csv=hcb.getCsv("http://ichart.yahoo.com/table.csv",[ s: 'GOOGL']);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		csv.eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry: ${obj}")}
	}
}
