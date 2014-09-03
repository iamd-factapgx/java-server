package eu.telecomnancy.factapgx.vendor.hadoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import eu.telecomnancy.factapgx.kernel.Kernel;
import eu.telecomnancy.factapgx.kernel.conf.Conf;


public class FSManager {
	
	private String hdfs;
	private Configuration configuration;
	
	public FSManager(Conf conf) {
		hdfs			= conf.hadoop().getHdfs();
		configuration	= new Configuration();
		System.setProperty("HADOOP_USER_NAME", "hduser");
		configuration.set("hadoop.job.ugi", "hduser");
		configuration.set("dfs.replication", Kernel.conf().hadoop().getReplication());
		configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	}
	
	/**
	 * Get HDFS
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public FileSystem getHdfs() throws IOException, URISyntaxException {
		return FileSystem.newInstance(new URI(hdfs), configuration);
	}
	
	/**
	 * Pubmed storage directory
	 * @return
	 */
	public String getRootDir() {
		return "/facta-pgx";
	}
	
	/**
	 * Publications storage directory
	 * @return
	 */
	public String getStorageDir() {
		return getRootDir() + "/storage";
	}
	
	public String getPublicationsStorageDir() {
		return getStorageDir() + "/publications";
	}
	
	/**
	 * Analytics dir for PIG
	 * @return
	 */
	public String getAnalyticsdir() {
		return getRootDir() + "/analytics";
	}

	public String getPublicationsYearStorageDirectory(int year) {
		return String.format("%s/%s", getPublicationsStorageDir(), year);
	}

	public String getYearAnalyticsDirectory(int year) {
		return String.format("%s/%s", getAnalyticsdir(), year);
	}
}
