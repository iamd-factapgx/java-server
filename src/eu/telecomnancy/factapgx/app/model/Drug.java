package eu.telecomnancy.factapgx.app.model;

import java.util.HashMap;
import java.util.Map;

public class Drug {
	
	private String id;
	
	private String name;
	
	private Map<String, Gene> relations;
	
	public Drug() {
		relations = new HashMap<String, Gene>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Gene> getRelations() {
		return relations;
	}

	public void setRelations(Map<String, Gene> relations) {
		this.relations = relations;
	}
	public String toString() {
		return String.format("{name: %s, relations: %s}", name, relations);
	}
}
