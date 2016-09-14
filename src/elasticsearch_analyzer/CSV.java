package elasticsearch_analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

public class CSV {
    
	// initiate CSV variable
	public static CSV csv;
	
	// create new CSV object if it does not exist
	public static CSV getInstance() throws IOException{
		if (csv == null){
			csv = new CSV();
		}
		return csv;
	}
	
	// initiate CsvWriter variable
	private CsvWriter csvOutput;
	
	private CSV() throws IOException{
        
		// check if CSV file exists, using name from config
		boolean csvExists = new File(Config.csv_File).exists();
		
		// create the CSV file with name from config
		csvOutput = new CsvWriter(new FileWriter(Config.csv_File, true), ',');
		
		// write the string variables to the CSV file if it exists
		if (!csvExists){
			write("Issue Number", "Issue Title", "Commit ID");
		}
		
	}
	
<<<<<<< HEAD
	public void write(String number, String title, String commitID) throws IOException{
=======
	// method to write the strings to the CSV file for each record
	public void write(String number, String title) throws IOException{
>>>>>>> af039599a017d920c189a722b2723333702ca295
		csvOutput.write(number);
		csvOutput.write(title);
		csvOutput.write(commitID);
		csvOutput.endRecord();
	}
	
	// method to close and write out the CSV file
	public void close(){
		csvOutput.close();
		csv = null;
	}
	
}
