package eu.telecomnancy.factapgx.vendor.mongo;

import java.net.UnknownHostException;

import com.mongodb.*;

import eu.telecomnancy.factapgx.kernel.conf.Conf;
import eu.telecomnancy.factapgx.kernel.conf.MongoConf;


public class MongoManager {
	
	private MongoConf conf;

	public MongoManager(Conf conf) {
		this.conf = conf.mongo();
	}
	
	public DB getDb() throws UnknownHostException, MongoException {	
		DB db = (new Mongo(conf.getAddress(), conf.getPort())).getDB(conf.getDb());
		if (conf.getUser() != null) {
			if (db.authenticate(conf.getUser(), conf.getPassword().toCharArray())) {
				return db;
			} else {
				throw new MongoException(String.format("MongoDB : Impossible to authenticate \"%s\" using password", conf.getUser()));
			}
		}
		return db;
	}
}
