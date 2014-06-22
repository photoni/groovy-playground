package service
import groovy.util.logging.Slf4j
import model.Security
import model.SecurityQuote
import net.HttpClientOps
import au.com.bytecode.opencsv.CSVReader
import au.com.bytecode.opencsv.bean.CsvToBean
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy


@Slf4j
class SecurityService {
	def Security loadSecurity(String symbol){
		def hcb=new HttpClientOps()
		long start= System.nanoTime()
		/* change this to Redis */
		def csvBinary=hcb.getCsvAsBinary("http://ichart.yahoo.com/table.csv",[ s: symbol])
		long end= System.nanoTime()
		log.debug("Total time nano : {}", end-start)
		CSVReader csvr= new CSVReader(new StringReader(new String(csvBinary)))
		//def csvLines=csvr.readAll()
		HeaderColumnNameTranslateMappingStrategy<SecurityQuote> strat= new HeaderColumnNameTranslateMappingStrategy<SecurityQuote>()
		strat.setType(SecurityQuote.class)
		Map<String, String> mapping = new HashMap<String, String>()
		mapping.put("Date", "dateAsString")
		mapping.put("Adj Close", "close")
		mapping.put("High", "high");
		mapping.put("Low", "low");
		mapping.put("Open", "open");
		mapping.put("Volume", "volume");		
		strat.setColumnMapping(mapping);

		CsvToBean csv = new CsvToBean();
		List<SecurityQuote> list = csv.parse(strat, csvr);
		Security security= new Security();
		security.setSymbol(symbol);
		security.setHistory(list);
		return security;
	}
}
