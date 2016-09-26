package elasticsearch_analyzer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Only 1 Stage at a time to be ran, comment out stages not in use
		
		// Stage 1 - Obtain Pull Requests that are closed and with a label of bug
		GitAccess.getInstance().Search(); 
		
		// Stage 2 - Obtain only Merged Pull Requests from data obtained in Stage 1
		GitAccess.getInstance().SearchIsMerged(); 
		
		// Stage 3 - Required merge commit sha from event chain as older pull requests doesn't hold the correct merge commit sha that is visible from the API 
		GitAccess.getInstance().SearchIsMergedRepairSha(); 
		
		// Stage 4 - Obtain Git Diff from Remote Github Server - very efficient in comparison to Jgit & Bash/Powershell
		GitAccess.getInstance().PullDiff();
		
		// Stage 5 - Obtain Git Commit Summary
		GitAccess.getInstance().ObtainSummaries();
		
		// Stage 6 - Combine Summary and Diff
		GitAccess.getInstance().CombineSummaryDiff();
	}

}
