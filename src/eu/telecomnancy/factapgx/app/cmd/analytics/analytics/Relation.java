package eu.telecomnancy.factapgx.app.cmd.analytics.analytics;

import java.util.List;

public class Relation {
	
	public String _id;
	public List<String> relations;
	public int type;
	public List<String> sentences;
	
	public String toString() {
		return relations.toString();
	}
}
