package eu.telecomnancy.factapgx.kernel.conf;

public class MongoConf {
	
	/**
	 * MongoDB address
	 */
	private String address;
	
	/**
	 * MongoDB port
	 */
	private int port;
	
	/**
	 * Mongo database name
	 */
	private String db;
	
	/**
	 * MongoDB username
	 */
	private String user;
	
	/**
	 * MongoDB password
	 */
	private String password;
	
	
	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getDb() {
		return db;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
