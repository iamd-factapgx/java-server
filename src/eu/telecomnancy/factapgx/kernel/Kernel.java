package eu.telecomnancy.factapgx.kernel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import eu.telecomnancy.factapgx.kernel.conf.Conf;
import eu.telecomnancy.factapgx.kernel.server.Server;
import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.elasticsearch.ElasticsearchManager;
import eu.telecomnancy.factapgx.vendor.hadoop.FSManager;
import eu.telecomnancy.factapgx.vendor.log.Logger;
import eu.telecomnancy.factapgx.vendor.mongo.MongoManager;


public class Kernel {
	
	private static Conf conf;
	private static Server server;
	private static File listener;
	
	public static void main(String[] args) {
		try {
			
			conf = (new Gson()).fromJson(new FileReader("conf/app.conf"), Conf.class);
			
			Logger.init(conf);
			
			listener = new File(conf.getLogs() + "/storage.log");
			buildContainer();
			startServer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}
	
	private static void buildContainer() {
		Container.set("fs", new FSManager(conf));
		Container.set("elasticsearch", new ElasticsearchManager(conf));
		Container.set("mongo", new MongoManager(conf));
		Container.set("gson", new Gson());
	}
	
	/**
	 * Start pubmed server
	 * @throws IOException
	 */
	public static void startServer() throws IOException {
		server = new Server(conf.server().getPort());
		server.start();
	}
	
	public static File getListener() {
		return listener;
	}
	
	public static Conf conf() {
		return conf;
	}
}
