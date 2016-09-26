package elasticsearch_analyzer;

import java.util.EnumMap;

import com.jcabi.github.Issues;
import com.jcabi.github.Issues.Qualifier;

public class Config {

	// OAuth token 
	public static final String git_OAuth = "4632ed7598221d6a99b34c3eaa5e851fd9fae79d";
	
	// Repository to be used
	public static final String git_Repository = "elastic/elasticsearch";
	
	public static final boolean git_AllIssues = true;
	
	// CSV file output name
	public static final String csv_File = "output.csv";
	
	// Method to get qualifier information
	public static EnumMap<Qualifier, String> getQualifier(){
		
		// enumMap object
		EnumMap<Issues.Qualifier,String> qualifiers = new EnumMap<Issues.Qualifier,String>(Issues.Qualifier.class);
		
		// qualifier for bug labels
		qualifiers.put(Issues.Qualifier.LABELS, "bug");
		
		// qualifier for closed state
		qualifiers.put(Issues.Qualifier.STATE, "closed");
		
		// return the qualifiers
		return qualifiers;
		
	}
	
}
