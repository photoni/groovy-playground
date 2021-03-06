package service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import model.Security
import model.SecurityConverter;
import model.SecurityQuote
import net.HttpClientOps
import au.com.bytecode.opencsv.CSVReader
import au.com.bytecode.opencsv.bean.CsvToBean
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy
import data.SecurityRepo
import org.codehaus.groovy.reflection.ReflectionUtils
import util.CSVUtil


/**
 * Entry point for security services
 * @author filippo
 *
 */
@Slf4j
@Singleton(strict=false)
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
		Security security = SecurityConverter.fromBinary(csvBinary, symbol,false);
		long end= System.nanoTime()
		//log.debug("Total time nano : {}", end-start)
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
		//log.debug("Total time nano : {}", end-start)
		
		
	}
	/**
	 * Get a security from the repository
	 * @param symbol
	 * @return
	 */
	Security getSecurity(String symbol){
		long start= System.nanoTime()	
		byte[] bytes=sr.getBinaryCurve(symbol)
		Security security=SecurityConverter.fromBinary(bytes,symbol,false)
		long end= System.nanoTime()
		//log.debug("Total time nano : {}", end-start)
		return security
		
	}

    /**
     * Get a security from the given file
     * @param symbol
     * @return
     */
    Security getSecurityFromCsv(String filePath, Map mapping,String dateFormat,boolean reverse){
        long start= System.nanoTime()
        byte[] bytes=CSVUtil.binary(filePath)

        Security security=SecurityConverter.fromBinary(bytes,mapping,dateFormat,'tmp-'+start,reverse)
        long end= System.nanoTime()
        //log.debug("Total time nano : {}", end-start)
        return security

    }

	/**
	 * Load prices in a json format from file system
	 * @param ticker
	 * @return
	 */
	def double[] getLegacyPrices(String ticker,String historyFolder) {
		def path = "/histories/${historyFolder}/${ticker}.json"
		InputStream is = ReflectionUtils.getCallingClass(0).getResourceAsStream(path)
		ObjectMapper mapper = new ObjectMapper();
		List list = (ArrayList) mapper.readValue(is, new ArrayList<Object>().class);
		double[] pricesT = new double[list.size()]
		for (int i = 0; i < list.size(); i++) {
			pricesT[i] = list.get(i)['v']
		}
		pricesT
	}

	/**
	 * Load prices in a json format from file system
	 * @param is
	 * @return
	 */
	def double[] getLegacyPrices(InputStream is) {
		ObjectMapper mapper = new ObjectMapper();
		List list = (ArrayList) mapper.readValue(is, new ArrayList<Object>().class);
		double[] pricesT = new double[list.size()]
		for (int i = 0; i < list.size(); i++) {
			pricesT[i] = list.get(i)['v']
		}
		pricesT
	}
	
	
}
