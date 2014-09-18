package util

import groovy.util.logging.Slf4j
import model.Security
import model.SecurityConverter

import org.junit.Test

import au.com.bytecode.opencsv.CSVReader


@Slf4j
class CSVTest {
    
	@Test
    public void loadTest() {
		CSVUtil.entries('cs-adx.csv').each {
			obj -> log.debug(" entry: ${obj}")
		}
//        //load and split the file
//        InputStream inputFile = getClass().classLoader.getResourceAsStream('cs-adx.csv')
//		CSVReader csvr= new CSVReader(new StringReader(new String(inputFile.getBytes())))
//		csvr.readAll().each {
//			obj -> log.debug(" entry: ${obj}")
//		}
    }

    
}
