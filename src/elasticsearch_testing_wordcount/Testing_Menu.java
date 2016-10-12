package elasticsearch_testing_wordcount;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Testing_Menu {
	
	public Testing_Menu()
	{
		int c1;

		System.out.println("Welcome!!");
		System.out.println("1. search the word.");
		System.out.println("2. exit.");
		
		Scanner mOrder = new Scanner(System.in);
		//mOrder.useDelimiter("\\n");
		System.out.println("Please enter a number:");
		c1 = mOrder.nextInt();
		
		/*do
		{
			System.out.println("Please enter a number:");
			c1 =Integer.parseInt(mOrder.nextLine());
		}while(c1 != 1 || c1 != 2);
		*/
		
		switch(c1)
		{
			case 1:
				Testing_Read.list.add(0);
				System.out.println("reading");
				System.out.println("Please enter the common word:");
				mOrder.nextLine();
				String cWord = mOrder.nextLine();
				if(cWord == null)
				{
					System.out.println("no input word.");
					break;
				}
				for (int x=4720;x<20572;x++)
				{
					String fileName = Integer.toString(x);
					Testing_Read file = new Testing_Read(fileName);
					try {
							file.openFile(cWord);
							System.out.println(x+" is read successfully.");
							// read the content and print
							/*int i;
							for (i=0; i< aryLines.length;i++)
							{
								//System.out.println(aryLines[i]);
								System.out.println(x+" is read successful.");
							}*/
						} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
				}
				
				Testing_Read.s = Testing_Read.s -1;
				System.out.println(cWord+": the total times "+Testing_Read.counter);
				System.out.println("The files are: "+ Arrays.asList(Testing_Read.list));
				System.out.println("The number of files contain this word: "+Testing_Read.s);
				break;
				
			case 2:
				System.out.println("closing");
				break;
		
		}
		mOrder.close();
	}

}
