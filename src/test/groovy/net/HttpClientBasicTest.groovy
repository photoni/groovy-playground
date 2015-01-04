package net

import helpers.ArrayHelper;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j
import net.HttpClientOps;

import org.junit.Test

import au.com.bytecode.opencsv.CSVReader;
@Slf4j
class HttpClientBasicTest {


	@Test
	void httpGetCSV() {
		def hcb=new HttpClientOps();
		long start= System.nanoTime();
		def csv=hcb.getCsv("http://ichart.yahoo.com/table.csv",[ s: 'GOOGL']);
		long end= System.nanoTime();
		log.debug("Total time nano : {}", end-start);
        ArrayHelper.log(csv,log,true,'TRACE')
		//csv.eachWithIndex { obj, i -> log.trace(" index: ${i}: - entry: ${obj}")}
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
        ArrayHelper.log(csvLines,log,true,'TRACE')
	}
}

