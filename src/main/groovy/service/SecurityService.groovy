package service
import groovy.util.logging.Slf4j
import model.Security
import model.SecurityConverter;
import model.SecurityQuote
import net.HttpClientOps
import au.com.bytecode.opencsv.CSVReader
import au.com.bytecode.opencsv.bean.CsvToBean
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy
import data.SecurityRepo


/**
 * Entry point for security services
 * @author filippo
 *
 */
@Slf4j
@Singleton
class SecurityService {		
	private SecurityRepo sr;

	static final String HTTP_ICHART_YAHOO_TABLE_CSV = "http://ichart.yahoo.com/table.csv"
		
	private SecurityService() {
		sr=SecurityRepo.instance;
	}
	
	/**
	 * Loads a security from the source
	 * @param symbol
	 * @return
	 */
	Security loadSecurity(String symbol){
		def hcb=new HttpClientOps()
		long start= System.nanoTime()
		
		byte[] csvBinary=hcb.getCsvAsBinary(HTTP_ICHART_YAHOO_TABLE_CSV,[ s: symbol])		
		Security security = SecurityConverter.fromBinary(csvBinary, symbol);
		long end= System.nanoTime()
		log.debug("Total time nano : {}", end-start)
		return security;
	}

	
	/**
	 * Loads a security from the source and store in the repository
	 * @param symbol
	 * @return
	 */
	void loadSecurityAndStore(String symbol){
		def hcb=new HttpClientOps()
		long start= System.nanoTime()
		byte[] csvBinary=hcb.getCsvAsBinary(HTTP_ICHART_YAHOO_TABLE_CSV,[ s: symbol])
		sr.storeBinaryCurve(symbol, csvBinary);
		long end= System.nanoTime()
		log.debug("Total time nano : {}", end-start)
		
		
	}
	/**
	 * Get a security from the repository
	 * @param symbol
	 * @return
	 */
	Security getSecurity(String symbol){
		long start= System.nanoTime()	
		byte[] bytes=sr.getBinaryCurve(symbol)
		Security security=SecurityConverter.fromBinary(bytes,symbol)
		long end= System.nanoTime()
		log.debug("Total time nano : {}", end-start)
		return security
		
	}
	
	
}
