package eu.telecomnancy.factapgx.app.model;

import java.util.List;

public abstract class Sentence {
	
	/**
	 * Sentence id
	 */
	private String _id;
	
	/**
	 * Publication id
	 */
	private String pid;
	
	/**
	 * List of diseases ids mentionned
	 */
	private List<String> disease;
	
	/**
	 * List of drugs ids mentionned
	 */
	private List<String> drug;

	/**
	 * List of genes ids mentionned
	 */
	private List<String> gene;
	
	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<String> getDisease() {
		return disease;
	}

	public void setDisease(List<String> disease) {
		this.disease = disease;
	}

	public List<String> getDrug() {
		return drug;
	}

	public void setDrug(List<String> drug) {
		this.drug = drug;
	}

	public List<String> getGene() {
		return gene;
	}

	public void setGene(List<String> gene) {
		this.gene = gene;
	}
}
