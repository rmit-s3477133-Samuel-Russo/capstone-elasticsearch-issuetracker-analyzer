package elasticsearch_analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.csvreader.CsvReader;
import com.jcabi.github.*;

public class GitAccess {

	private static GitAccess GitAccess;
	
	// Create new GitAccess object if it does not exist
	public static GitAccess getInstance(){
		
		if (GitAccess == null){
			GitAccess = new GitAccess();
		}
		
		return GitAccess;
	}
	
	// Initializing object variables
	private Github github;
	private Repo repository;
	private Issues issues;
	private Pulls pull;
	private RepoCommits commits;
	
	// Constructor for GitAccess
	private GitAccess(){
		// Create Github object using OAuth token from Config class
		github = new RtGithub(Config.git_OAuth);
		
		// Repository object using information from Config class
		repository = github.repos().get(new Coordinates.Simple(Config.git_Repository));
		
		// Objects for issues, pulls and commits
		issues = repository.issues();
		pull = repository.pulls();
		commits = repository.commits();
	}
	
	// Method used to search for particular pull requests/issues
	public void Search(){
		Iterable<Issue> iIssues = null;
		try {
			// Issues ordered in descending order with qualifier information from Config class
			iIssues = issues.search(Issues.Sort.UPDATED, Search.Order.DESC, Config.getQualifier());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Initializing pull number and commit record variables
		int pullnumber = 0;
		int commitrecord = 0;
		for(Iterator<Issue> issueIndex = iIssues.iterator(); issueIndex.hasNext(); ) {
			
			try {

				Issue.Smart issueItem = new Issue.Smart(issueIndex.next());
			    
			    if(issueItem.isPull()){
			    	Pull.Smart pullrequest = new Pull.Smart(issueItem.pull());   
			    	
			    	// Print statement for testing purposes
			    	System.out.println("Pull request Number Processing - " + ++pullnumber + " - " + pullrequest.number());
			    	if (!pullrequest.isOpen()){
			    		// Get merge commit id from JSON
			    		String merge_commit_id = pullrequest.json().getString("merge_commit_sha");
			    		System.out.println("Merge Commit Number Written - " + ++commitrecord + " - " + merge_commit_id);
			    		
			    		// Input information to CSV and write it out
			    		CSV.getInstance().write(String.valueOf(pullrequest.number()), pullrequest.title(), merge_commit_id);
			    		CSV.getInstance().close();
			    	}
			    	
			    }
			} catch (Exception e){
				e.printStackTrace();
				continue;
			}
		    
		}
		
	}
	
	// Method to merge the data into a csv file
	public void SearchIsMerged(){
		try {
			// CSV object with name
			CsvReader cv = new CsvReader("mergecommitsha.csv");
			
			// Initializing variables for record number and written record
			int recordnumber = 0;
			int writtenrecord = 0;
			cv.readHeaders();
			
			// While loop to read each record of CSV file
			while(cv.readRecord()){
				
				// Getting issue number, title and commit ID
				String inum = cv.get("Issue Number");
				String ititle = cv.get("Issue Title");
				String isha = cv.get("Commit ID");
				
				Pull.Smart pullrequest = new Pull.Smart(pull.get(Integer.parseInt(inum)));
				
				// Printing out each record number for testing purposes
				System.out.println("Processing Record - " + ++recordnumber);
				
				boolean isMergedTrue = pullrequest.json().getBoolean("merged");
				if (isMergedTrue){
					// Inputing information and writing it out
					CSV.getInstance().write(inum, ititle, isha);
		    		CSV.getInstance().close();
		    		System.out.println("Written Record - " + ++writtenrecord);
				}
				
			}
			
			// Exception capturing
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void SearchIsMergedRepairSha(){
		try {
			CsvReader cv = new CsvReader("mergecommitshaismerged.csv");
			int recordnumber = 0;
			cv.readHeaders();
			
			while(cv.readRecord()){
				String inum = cv.get("Issue Number");
				String ititle = cv.get("Issue Title");
				String isha = cv.get("Commit ID");
				
		
				System.out.println("Processing Record - " + ++recordnumber + " - " + inum);
				

				Pull.Smart pullrequest = new Pull.Smart(pull.get(Integer.parseInt(inum)));
				Issue.Smart issue = new Issue.Smart(pullrequest.issue());
				Iterable<Event> Events = issue.events();
				for(Iterator<Event> eventIndex = Events.iterator(); eventIndex.hasNext(); ){
					Event.Smart event = new Event.Smart(eventIndex.next());
					String checkMerged = event.json().getString("event");
					if (checkMerged.matches("merged")){
						String commit_id = event.json().getString("commit_id");
						isha = commit_id;
						CSV.getInstance().write(inum, ititle, isha);
			    		CSV.getInstance().close();
			    		System.out.println("Written New Sha - " + isha);
						break;
					}
				}
				
				
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to pull git diff information
	public void PullDiff(){
		try {
			
			// CSV object with name
			CsvReader cv = new CsvReader("mergecommitshaismergedfixed.csv");
			int recordnumber = 0;
			cv.readHeaders();
			
			// WHile loop to read each record of CSV file
			while(cv.readRecord()){
				String inum = cv.get("Issue Number");
				
				// URL for elastic search pull data
				URL website = new URL("https://github.com/elastic/elasticsearch/pull/" + inum + ".diff");
		        URLConnection connection = website.openConnection();		        
		        BufferedReader in = new BufferedReader(
		                                new InputStreamReader(
		                                    connection.getInputStream()));

		        StringBuilder response = new StringBuilder();
		        String inputLine;
                
		        // While a line is not blank, go to a new line
		        while ((inputLine = in.readLine()) != null) 
		            response.append(inputLine + "\r\n");

		        in.close();
		        
		        File fileDir = new File(inum + ".txt");
				
				Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileDir), "UTF8"));
				
				out.append(response);
				out.close();
				
				System.out.println("Written Record - " + ++recordnumber + " - " + inum);
				
			}

			
			// Exception capturing
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to get summaries
	public void ObtainSummaries(){
		try {
			CsvReader cv = new CsvReader("mergecommitshaismergedfixed.csv");
			int recordnumber = 0;
			cv.readHeaders();
			
			// read each record of the CSV file
			while(cv.readRecord()){
				String inum = cv.get("Issue Number");
				String isha = cv.get("Commit ID");

				System.out.println("Processing Record - " + ++recordnumber + " - " + inum);		
				
				
				RepoCommit.Smart commit = new RepoCommit.Smart(commits.get(isha));
				
				// String builder object
				StringBuilder commitSummary = new StringBuilder();
				
				// Append commit information
				commitSummary.append("Commit: " + isha + "\r\n");
				
				// JSON object created
				JsonObject commitJson = commit.json();
				
				JsonArray commitParentArray = commitJson.getJsonArray("parents");
				
				for(Object parentIndex : commitParentArray){
					JsonObject parent = (JsonObject)parentIndex;
					commitSummary.append("Parent: " + parent.getString("sha") + "\r\n");
				}
				
				
				JsonObject commitAuthor = commitJson.getJsonObject("commit").getJsonObject("author");
				commitSummary.append("Author: " + commitAuthor.getString("name") + " <" + commitAuthor.getString("email") + "> \r\n\r\n");
				
				commitSummary.append(commit.message() + "\r\n\r\n");
				
				// create text file 
				File fileDir = new File(inum + ".txt");
				
				Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileDir), "UTF8"));
				
				// Append the commit summary to the text file
				out.append(commitSummary);
				
				// Write out the file
				out.close();
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to combine the summary and git diff information
	public void CombineSummaryDiff(){
		try {
			CsvReader cv = new CsvReader("mergecommitshaismergedfixed.csv");
			int recordnumber = 0;
			cv.readHeaders();
			
			while(cv.readRecord()){
				String inum = cv.get("Issue Number");
				String ititle = cv.get("Issue Title");
						
				System.out.println("Processing Record - " + ++recordnumber + " - " + inum);
				
				StringBuilder output = new StringBuilder();
				
				// create text files for summary diff and dir
				File fileSum = new File("sum/" + inum + ".txt");
				File fileDiff = new File("diff/" + inum + ".txt");
				File fileDir = new File(inum + ".txt");
				
				// Append with Issue number and title with new lines
				output.append("Issue Number: " + inum + "\r\n");
				output.append("Issue Title: " + ititle + "\r\n\r\n");
				
				BufferedReader in1 = new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileSum)));
				String inputLine1;
				while ((inputLine1 = in1.readLine()) != null) 
					output.append(inputLine1 + "\r\n");

		        in1.close();
		        
		        output.append("\r\n");
				
		        String inputLine2;
		        BufferedReader in2 = new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileDiff)));
				
				while ((inputLine2 = in2.readLine()) != null) 
					output.append(inputLine2 + "\r\n");

		        in2.close();
		        
				
				Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileDir), "UTF8"));
				
				out.append(output);
				out.close();
				
			}
			// exception handling
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
