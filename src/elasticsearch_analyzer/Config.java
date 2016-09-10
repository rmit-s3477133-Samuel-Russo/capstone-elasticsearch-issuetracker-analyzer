package elasticsearch_analyzer;

import java.util.EnumMap;

import com.jcabi.github.Issues;
import com.jcabi.github.Issues.Qualifier;

public class Config {

	public static final String git_OAuth = "4632ed7598221d6a99b34c3eaa5e851fd9fae79d";
	public static final String git_Repository = "elastic/elasticsearch";
	public static final boolean git_AllIssues = true;
	
	public static final String csv_File = "output.csv";
	
	public static EnumMap<Qualifier, String> getQualifier(){
		
		EnumMap<Issues.Qualifier,String> qualifiers = new EnumMap<Issues.Qualifier,String>(Issues.Qualifier.class);
		qualifiers.put(Issues.Qualifier.LABELS, "bug");
		qualifiers.put(Issues.Qualifier.STATE, "closed");
		
		return qualifiers;
		
	}
	
}
