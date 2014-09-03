package eu.telecomnancy.factapgx.app.model;

public class Gene {
	
	private String id;
	
	private String name;
	
	private double score;

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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	public String toString() {
		return String.format("{name: %s, score: %s}", name, score);
	}
}
