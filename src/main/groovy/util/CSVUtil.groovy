package util

import au.com.bytecode.opencsv.CSVReader
import au.com.bytecode.opencsv.CSVWriter

class CSVUtil {
	
	static def entriesFromClassPath(filePath){
		//load and split the file
		InputStream inputFile = CSVUtil.class.classLoader.getResourceAsStream(filePath)
		CSVReader csvr= new CSVReader(new StringReader(new String(inputFile.getBytes())))
		csvr.readAll()
	}

	static def entriesFromURI(filePath){
		//load and split the file
		InputStream inputFile = new FileInputStream(filePath)
		CSVReader csvr= new CSVReader(new StringReader(new String(inputFile.getBytes())))
		csvr.readAll()
	}

    static def binary(filePath){
        //load and split the file
        InputStream inputFile = CSVUtil.class.classLoader.getResourceAsStream(filePath)
        inputFile.getBytes()
    }
	static def write(filePath,List<String[]> allLines){
		FileWriter fw=new FileWriter(filePath)
		CSVWriter csvw= new CSVWriter(fw);
		csvw.writeAll(allLines)
		csvw.flush()
		csvw.close()
	}



}
