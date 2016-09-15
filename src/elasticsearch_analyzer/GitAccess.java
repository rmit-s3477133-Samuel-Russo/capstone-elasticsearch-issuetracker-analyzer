package elasticsearch_analyzer;

import java.io.IOException;
import java.util.Iterator;

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
	
	private GitAccess(){
		github = new RtGithub(Config.git_OAuth);
		repository = github.repos().get(new Coordinates.Simple(Config.git_Repository));
		issues = repository.issues();
	}
	
	public void Search() throws IOException{
		Iterable<Issue> iIssues = issues.search(Issues.Sort.UPDATED, Search.Order.DESC, Config.getQualifier());
		int issuenumber = 0;
		int eventrecord = 0;
		int requestnumber = 0;
		for(Iterator<Issue> issueIndex = iIssues.iterator(); issueIndex.hasNext(); ) {
			
			Issue issumeItem = issueIndex.next();

		    Issue.Smart issueItemFull = new Issue.Smart(issumeItem);
		    
		    Iterable<Event> iEvents = issueItemFull.events();
		    System.out.println("Issue Number Processing - " + ++issuenumber);
		    System.out.println("Request Number - " + ++requestnumber);
		    for(Iterator<Event> eventIndex = iEvents.iterator(); eventIndex.hasNext(); ){
		    	Event.Smart eventItemFull = new Event.Smart(eventIndex.next());
		    	System.out.println("Request Number - " + ++requestnumber);
		    	if (!eventItemFull.json().isNull("commit_id")){
		    		String commit_id = eventItemFull.json().getString("commit_id");
		    		CSV.getInstance().write(String.valueOf(issueItemFull.number()), issueItemFull.title(), commit_id);
		    		System.out.println("Event Records Written - " + ++eventrecord);
		    	}
		    }  
		    CSV.getInstance().close();
		}
		
	}
	
}
