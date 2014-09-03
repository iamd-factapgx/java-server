package eu.telecomnancy.factapgx.app.cmd.diseases;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import eu.telecomnancy.factapgx.app.cmd.diseases.exception.DiseasesCmdException;
import eu.telecomnancy.factapgx.kernel.cmd.CmdInterface;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;
import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.mongo.MongoException;
import eu.telecomnancy.factapgx.vendor.mongo.MongoManager;

public class DiseasesCmd implements CmdInterface {

	@Override
	public Object process(String data) throws CmdException {
		try {
			Gson gson				= Container.get("gson", Gson.class);
			MongoManager mongo		= Container.get("mongo", MongoManager.class);
		
			DB db = mongo.getDb();
			DBCollection collection	= db.getCollection("diseases");
			
			DBCursor cursor			= collection.find(new BasicDBObject("disease", Pattern.compile("^"+data+".*"))).limit(10);
			
			List<Disease> diseases = new ArrayList<Disease>();
			while (cursor.hasNext()) {
				DBObject o	= cursor.next();
				String id	= (String) o.get("_id");
				
				Disease disease = new Disease();
				disease.setId(id);
				
				BasicDBList ds	= (BasicDBList) o.get("disease");
				if (ds.size() > 0) {
					disease.setName((String) ds.get(0));
				} else {
					disease.setName(id);
				}
				diseases.add(disease);
			}
			
			return gson.toJson(diseases);
		} catch (UnknownHostException | MongoException e) {
			throw new DiseasesCmdException(e.getMessage(), e);
		}
	}

}
