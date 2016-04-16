package util

import groovy.util.logging.Slf4j
import org.junit.Test

@Slf4j
class CSVTest {
    
	@Test
    public void loadTest() {
		CSVUtil.entriesFromClassPath('cs-adx.csv').each {
			obj -> log.debug(" entry: ${obj}")
		}

    }

	@Test
	public void writeTest() {
		String[] header=['col1','col2']
		String[] line1=['val11','val12']
		String[] line2=['val21','val22']
		def lines=[header,line1,line2]
		CSVUtil.write('/var/data/pig/test.csv',lines);

	}

    
}
