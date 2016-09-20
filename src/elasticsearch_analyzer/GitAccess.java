package elasticsearch_analyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import com.csvreader.CsvReader;
import com.jcabi.github.*;

public class GitAccess {

	private static GitAccess GitAccess;
	
	public static GitAccess getInstance(){
		
		if (GitAccess == null){
			GitAccess = new GitAccess();
		}
		
		return GitAccess;
	}
	
	private Github github;
	private Repo repository;
	private Issues issues;
	private Pulls pull;
	
	private GitAccess(){
		github = new RtGithub(Config.git_OAuth);
		repository = github.repos().get(new Coordinates.Simple(Config.git_Repository));
		issues = repository.issues();
		pull = repository.pulls();
	}
	
	public void Search(){
		Iterable<Issue> iIssues = null;
		try {
			iIssues = issues.search(Issues.Sort.UPDATED, Search.Order.DESC, Config.getQualifier());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int pullnumber = 0;
		int commitrecord = 0;
		for(Iterator<Issue> issueIndex = iIssues.iterator(); issueIndex.hasNext(); ) {
			
			try {

				Issue.Smart issueItem = new Issue.Smart(issueIndex.next());
			    
			    if(issueItem.isPull()){
			    	Pull.Smart pullrequest = new Pull.Smart(issueItem.pull());   
			    	System.out.println("Pull request Number Processing - " + ++pullnumber + " - " + pullrequest.number());
			    	if (!pullrequest.isOpen()){
			    		String merge_commit_id = pullrequest.json().getString("merge_commit_sha");
			    		System.out.println("Merge Commit Number Written - " + ++commitrecord + " - " + merge_commit_id);
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
	
	public void SearchIsMerged(){
		try {
			CsvReader cv = new CsvReader("mergecommitsha.csv");
			int recordnumber = 0;
			int writtenrecord = 0;
			cv.readHeaders();
			
			while(cv.readRecord()){
				String inum = cv.get("Issue Number");
				String ititle = cv.get("Issue Title");
				String isha = cv.get("Commit ID");
				
				Pull.Smart pullrequest = new Pull.Smart(pull.get(Integer.parseInt(inum)));
				System.out.println("Processing Record - " + ++recordnumber);
				
				boolean isMergedTrue = pullrequest.json().getBoolean("merged");
				if (isMergedTrue){
					CSV.getInstance().write(inum, ititle, isha);
		    		CSV.getInstance().close();
		    		System.out.println("Written Record - " + ++writtenrecord);
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
	
}
