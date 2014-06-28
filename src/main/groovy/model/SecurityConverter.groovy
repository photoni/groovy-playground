package model

import au.com.bytecode.opencsv.CSVReader
import au.com.bytecode.opencsv.bean.CsvToBean
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy

/**
 * Converter class for {@link Security}
 * @author filippo
 *
 */
class SecurityConverter {
	/* INTERNALS */
	
	/**
	 * Convert a binary csv stream to a security. 
	 * @param csvBinary
	 * @param symbol
	 * @return
	 */
	static  Security fromBinary(byte[] csvBinary, String symbol) {
		CSVReader csvr= new CSVReader(new StringReader(new String(csvBinary)))
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
		security.setSymbol(symbol)
		security.setHistory(list)
		security.getHistory().each { obj -> def Date date=Date.parse("yyyy-MM-dd",obj.dateAsString);obj.setDate(date.getTime())}
		return security
	}

}
