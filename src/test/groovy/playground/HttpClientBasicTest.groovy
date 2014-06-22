package playground;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import net.HttpClientOps;

import org.junit.Test

import au.com.bytecode.opencsv.CSVReader;
@Slf4j
class HttpClientBasicTest {

	@Test
	void httpGetJsonTest() {
		def hcb=new HttpClientOps();
		String resp=hcb.getHttpSimple("http://google.com");
		long start= System.nanoTime();
		def json=hcb.getJson("http://api.openweathermap.org/data/2.5/",[ path: 'weather', query: [q: 'London']]);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		log.debug("It is currently ${json.weather.description} in London.");
	}

	@Test
	void httpGetCSV() {
		def hcb=new HttpClientOps();
		long start= System.nanoTime();
		def csv=hcb.getCsv("http://ichart.yahoo.com/table.csv",[ s: 'GOOGL']);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		csv.eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry: ${obj}")}
	}
	@Test
	void httpGetCSVBinary() {
		def hcb=new HttpClientOps();
		long start= System.nanoTime();
		def csvBinary=hcb.getCsvAsBinary("http://ichart.yahoo.com/table.csv",[ s: 'GOOGL']);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
		CSVReader csvr= new CSVReader(new StringReader(new String(csvBinary)))
		def csvLines=csvr.readAll();
		csvLines.eachWithIndex { obj, i -> log.debug(" index: ${i}: - entry: ${obj}")}
	}
}

