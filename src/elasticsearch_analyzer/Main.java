package elasticsearch_analyzer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Stage 1 - Obtain Pull Requests that are closed and with a label of bug
		GitAccess.getInstance().Search(); 
		// Stage 2 - Obtain only Merged Pull Requests from data obtained in Stage 1
		GitAccess.getInstance().SearchIsMerged(); 
		// Stage 3 - Required merge commit sha from event chain as older pull requests doesn't hold the correct merge commit sha that is visible from the API 
		GitAccess.getInstance().SearchIsMergedRepairSha(); 
	}

}
