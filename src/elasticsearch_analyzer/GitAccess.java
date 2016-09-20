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
	
}
