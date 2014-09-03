package eu.telecomnancy.factapgx.app.model;

import java.util.HashMap;
import java.util.Map;

public class Disease {
	
	private String id;
	
	private String name;
	
	private Map<String, Drug> relations;
	
	public Disease() {
		relations = new HashMap<String, Drug>();
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

	public Map<String, Drug> getRelations() {
		return relations;
	}

	public void setRelations(Map<String, Drug> relations) {
		this.relations = relations;
	}
	
	public String toString() {
		return String.format("{name: %s, relations: %s}", name, relations);
	}
}
