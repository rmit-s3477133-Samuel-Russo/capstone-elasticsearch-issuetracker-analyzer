package elasticsearch_analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

public class CSV {

	public static CSV csv;
	
	public static CSV getInstance() throws IOException{
		if (csv == null){
			csv = new CSV();
		}
		return csv;
	}
	
	private CsvWriter csvOutput;
	
	private CSV() throws IOException{
		boolean csvExists = new File(Config.csv_File).exists();
		
		csvOutput = new CsvWriter(new FileWriter(Config.csv_File, true), ',');
		
		if (!csvExists){
			write("Issue Number", "Issue Title");
		}
		
	}
	
	public void write(String number, String title) throws IOException{
		csvOutput.write(number);
		csvOutput.write(title);
		csvOutput.endRecord();
	}
	
	public void close(){
		csvOutput.close();
		csv = null;
	}
	
}
