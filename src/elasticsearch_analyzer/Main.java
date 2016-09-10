package elasticsearch_analyzer;

import java.io.IOException;
import java.util.Iterator;

import com.jcabi.github.*;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Github github = new RtGithub(Config.git_OAuth);
		Repo repository = github.repos().get(new Coordinates.Simple(Config.git_Repository));
		
		Issues issues = repository.issues();
		
		Iterable<Issue> iIssues = issues.search(Issues.Sort.UPDATED, Search.Order.DESC, Config.getQualifier());
		
		for(Iterator<Issue> issueIndex = iIssues.iterator(); issueIndex.hasNext(); ) {
			
			Issue issumeItem = issueIndex.next();

		    Issue.Smart issueItemFull = new Issue.Smart(issumeItem);

		    System.out.println(issueItemFull.title() + " - " + issueItemFull.number());
		    
		}
		
	}

}
