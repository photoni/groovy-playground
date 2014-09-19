package util

import au.com.bytecode.opencsv.CSVReader

class CSVUtil {
	
	static def entries(filePath){
		//load and split the file
		InputStream inputFile = CSVUtil.class.classLoader.getResourceAsStream(filePath)
		CSVReader csvr= new CSVReader(new StringReader(new String(inputFile.getBytes())))
		csvr.readAll()
	}
    static def binary(filePath){
        //load and split the file
        InputStream inputFile = CSVUtil.class.classLoader.getResourceAsStream(filePath)
        inputFile.getBytes()
    }

}
