package eu.telecomnancy.factapgx.kernel.conf;

public class Conf {
	
	/**
	 * Whether debug is enabled
	 */
	private boolean debug;
	
	/**
	 * Server logs dir
	 */
	private String logs;
	
	/**
	 * Server configuration
	 */
	private ServerConf server;
	
	/**
	 * Hadoop configuration
	 */
	private HadoopConf hadoop;
	
	/**
	 * Elasticsearch conf
	 */
	private EsConf es;
	
	/**
	 * Mongo configuration
	 */
	private MongoConf mongo;

	/**
	 * Pig server configuration
	 */
	private MapredConf mapred;
	
	
	public boolean isDebug() {
		return debug;
	}
	
	public String getLogs() {
		return logs;
	}
	
	public ServerConf server() {
		return server;
	}
	
	public HadoopConf hadoop() {
		return hadoop;
	}
	
	public EsConf es() {
		return es;
	}

	public MongoConf mongo() {
		return mongo;
	}
	
	public MapredConf mapred() {
		return mapred;
	}
}
