package elasticsearch_testing_wordcount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Testing_Read {
	
	private String path;
	private int fName;
	public static int counter=0, s=0;
	public static List<Integer> list = new ArrayList<Integer>();
	

	public Testing_Read(String fileName) {
		// TODO Auto-generated method stub
		path = "F:\\RMIT Study\\RMIT BIT\\SEM4\\P1\\Elastic Search Pull Request Data\\commit\\"+fileName+".txt";
		fName = Integer.valueOf(fileName);
	}
	
	public String[] openFile(String cWord) throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		String[] textData = new String[readLines()];
		
		int i;
		
		for(i=0; i< readLines(); i++)
		{
			textData[i] = textReader.readLine();
			while ((textData[i] = textReader.readLine()) != null) {
			    
				String[] words = textData[i].split(" ");
				for(String word : words)
				{
					if (word.equals(cWord))
				    {
				    	counter++;
				    }
					if(word.compareTo(cWord) == 0)
					{
	
						if (fName != list.get(s))
						{
							list.add(fName);
							s++;
						}
		
					}
				}
			    
			    // Now you have a String array containing each word in the current line
			}
		}
		textReader.close();
		return textData;
	}
	
	int readLines() throws IOException
	{
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);

		int numberOfLines = 0;
		
		while(bf.readLine() != null)
		{
			numberOfLines++;
		}
		bf.close();
		
		return numberOfLines;
	}

}
