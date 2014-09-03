package eu.telecomnancy.factapgx.app.model;

public class Publication {
	
	/**
	 * Publication id
	 */
	private String _id;
	
	/**
	 * Publication title
	 */
	private String title;
	
	/**
	 * Publication abstract
	 */
	private String abstrct;
	
	/**
	 * Publication year
	 */
	private int year;
	
	/**
	 * Publication number of sentences
	 */
	private int sentences;
	
	
	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstrct() {
		return abstrct;
	}

	public void setAbstrct(String abstrct) {
		this.abstrct = abstrct;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public int getSentences() {
		return sentences;
	}

	public void setSentences(int sentences) {
		this.sentences = sentences;
	}
	
	public boolean valid() {
		return	_id		!= null	&& !_id.isEmpty()		&&
				title	!= null	&& !title.isEmpty()		&&
				abstrct	!= null	&& !abstrct.isEmpty()	&&
				year > 1900;
	}
}
