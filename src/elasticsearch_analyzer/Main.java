package elasticsearch_analyzer;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		
		// Creating a Github object using OAuth token from config class
		Github github = new RtGithub(Config.git_OAuth);
		
		// Creating a Repo object that uses the elasic search repository from config class
		Repo repository = github.repos().get(new Coordinates.Simple(Config.git_Repository));
		
		// Creating an Issues object from the repository
		Issues issues = repository.issues();
		
		// Creating an object that can search issues in descending order and use qualifiers in getQualifier method from config class
		Iterable<Issue> iIssues = issues.search(Issues.Sort.UPDATED, Search.Order.DESC, Config.getQualifier());
		
		// Iterating through the issues for closed bugs in descending order
		for(Iterator<Issue> issueIndex = iIssues.iterator(); issueIndex.hasNext(); ) {
			
			Issue issumeItem = issueIndex.next();

		    Issue.Smart issueItemFull = new Issue.Smart(issumeItem);
            
		    // Prints the issue name and number on each line
		    System.out.println(issueItemFull.title() + " - " + issueItemFull.number());
		    
		}
=======
		
		GitAccess.getInstance().Search();
>>>>>>> origin/master
		
	}

}
