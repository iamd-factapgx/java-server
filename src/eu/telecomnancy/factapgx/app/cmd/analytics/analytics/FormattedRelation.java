package eu.telecomnancy.factapgx.app.cmd.analytics.analytics;

import java.util.Map;

public class FormattedRelation {
	private String id;
	private int type;
	private Map<String, FormattedRelation> relations;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<String, FormattedRelation> getRelations() {
		return relations;
	}

	public void setRelations(Map<String, FormattedRelation> relations) {
		this.relations = relations;
	}
}
