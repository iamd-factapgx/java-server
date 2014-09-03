package eu.telecomnancy.factapgx.app.cmd.analytics.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import eu.telecomnancy.factapgx.app.model.Disease;
import eu.telecomnancy.factapgx.app.model.Drug;
import eu.telecomnancy.factapgx.app.model.Gene;
import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.mongo.MongoManager;

public class RelationLoader {
	
	private String id;
	
	/**
	 * 1 => Disease	-> Drug		-> Gene
	 * 2 => Disease	-> Gene 	-> Drug
	 * 3 => Drug	-> Disease	-> Gene
	 * 4 => Drug	-> Gene		-> Disease
	 * 5 => Gene	-> Disease	-> Drug
	 * 6 => Gene	-> Drug		-> Disease
	 */
	private String type;
	
	/**
	 * 1 => Disease	-> Drug
	 * 2 => Disease	-> Gene
	 * 3 => Drug	-> Gene
	 */
	private Map<Integer, Map<String, Relation>> relations;
	
	private List<String> pids;
	private Map<String, Double> ds;
	private Map<String, Double> rs;
	private Map<String, Double> probabilities;

	private long publications;

	public RelationLoader(String id, String type) {
		this.id				= id;
		this.type 			= type;
		this.relations		= new HashMap<Integer, Map<String, Relation>>();
		for (int i=0; i < 4; i++) {
			this.relations.put(i, new HashMap<String, Relation>());
		}
		
		this.pids			= new ArrayList<String>();

		this.ds				= new HashMap<String, Double>();
		this.rs				= new HashMap<String, Double>();
		this.probabilities	= new HashMap<String, Double>();
	}
	
	/**
	 * Fetch relations
	 * @return
	 * @throws Exception
	 */
	public String fetch() throws Exception {
		MongoManager mongo		= Container.get("mongo", MongoManager.class);
		DB db					= mongo.getDb();	
		
		BasicDBObject results	= (BasicDBObject) db.eval("db.publications.aggregate({ $group: { _id: null, total: { $sum: \"$sentences\" } } } )");
		BasicDBList res			= (BasicDBList) results.get("_firstBatch");
		
		publications			= ((BasicDBObject) res.get(0)).getLong("total");
		
		DBCollection rc			= db.getCollection("relations");
		DBCursor cursor			= rc.find(new BasicDBObject("relations", id));
		
		while (cursor.hasNext()) {
			addRelation(cursor.next(), true);
		}
		
		if (!this.relations.get(0).containsKey(this.id + "-0")) {
			return String.format("{\"id\": \"%s\", \"relations\":{}}", this.id);
		}
		
		int type;
		switch (this.type) {
			case "1":
				type = 3;
				break;
			case "2":
				type = 3;
				break;
			case "3":
				type = 2;
				break;
			case "4":
				type = 2;
				break;
			case "5":
				type = 1;
				break;
			default:
				type = 1;
				break;
		}
		
		BasicDBList or = new BasicDBList();
		or.add(new BasicDBObject("type", 0));
		or.add(new BasicDBObject("type", type));
		cursor			= rc.find((new BasicDBObject("relations", new BasicDBObject("$in", pids))).append("$or", or));
		while (cursor.hasNext()) {
			addRelation(cursor.next(), false);
		}
		
		db.getMongo().close();
		
		
		FormattedRelation formattedRelation;
		switch (this.type) {
			case "1":
				formattedRelation = format(2, 1, 3);
				break;
			default:
				formattedRelation = format(1, 2, 3);
				break;
		}
		try {
			calculateProbabilities(formattedRelation);
		} catch (Exception e) {
			e.printStackTrace();
			return String.format("{\"id\": \"%s\", \"relations\":{}}", this.id);
		}
		return (new Gson()).toJson(format(calculateScores(formattedRelation)));
	}
	
	/**
	 * Format results with names
	 * @param scores
	 * @return
	 * @throws Exception
	 */
	private Disease format(Map<String, Map<String, Double>> scores) throws Exception {
		MongoManager mongo							= Container.get("mongo", MongoManager.class);
		DB db										= mongo.getDb();
		
		// Disease
		Disease disease = new Disease();
		disease.setId(id);
		
		DBObject d				= db.getCollection("diseases").findOne(new BasicDBObject("_id", id));
		BasicDBList diseases	= (BasicDBList) d.get("disease");
		
		if (diseases.size() > 0) {
			disease.setName((String) diseases.get(0));
		} else {
			disease.setName(id);
		}
		
		
		// Associated drugs
		DBCursor cursor 			= db.getCollection("drugs").find(new BasicDBObject("_id", new BasicDBObject("$in", scores.keySet())));
		Map<String, String> names	= new HashMap<String, String>();
		
		while(cursor.hasNext()) {
			DBObject o = cursor.next();
			
			BasicDBList drugs	= (BasicDBList) o.get("drug");
			
			if (drugs.size() > 0) {
				names.put((String) o.get("_id"), (String) drugs.get(0));
			}
		}
		
		for (Entry<String, Map<String, Double>> e : scores.entrySet()) {
			if (e.getValue().entrySet().size() <= 0) {
				continue;
			}
			
			if (!names.containsKey(e.getKey())) {
				names.put(e.getKey(), e.getKey());
			}
			
			// Associated drugs
			Drug drug = new Drug();
			drug.setId(e.getKey());
			drug.setName(names.get(e.getKey()));
			
			
			// Associated genes
			DBCursor gCursor 			= db.getCollection("genes").find(new BasicDBObject("_id", new BasicDBObject("$in", e.getValue().keySet())));
			Map<String, String> gNames	= new HashMap<String, String>();
					
			while(gCursor.hasNext()) {
				DBObject o = gCursor.next();
				
				BasicDBList gs	= (BasicDBList) o.get("gene");
				
				if (gs.size() > 0) {
					gNames.put((String) o.get("_id"), (String) gs.get(0));
				}
			}
			
			for (Entry<String, Double> entry : e.getValue().entrySet()) {
				if (!gNames.containsKey(entry.getKey())) {
					gNames.put(entry.getKey(), entry.getKey());
				}
				
				Gene gene = new Gene();
				gene.setId(entry.getKey());
				gene.setName(gNames.get(entry.getKey()));
				gene.setScore(entry.getValue());
				
				drug.getRelations().put(entry.getKey(), gene);
			}
			
			disease.getRelations().put(e.getKey(), drug);
		}
		db.getMongo().close();
		
		return disease;
	}
	
	/**
	 * Create an instance of formatted relation
	 * @param id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private FormattedRelation baseFormattedRelation(String id, int type) throws Exception {
		FormattedRelation relation = new FormattedRelation();
		String rid		= id+"-0";
		Relation r		= this.relations.get(0).get(rid);
		
		if (r == null) {
			return null;
		}
		
		relation.setId(r.relations.get(0));
		relation.setType(type);
		relation.setRelations(new HashMap<String, FormattedRelation>());
		return relation;
	}
	
	/**
	 * 
	 * @param root
	 * @param type
	 * @param type2
	 * @return
	 * @throws Exception
	 */
	private FormattedRelation format(int root, int type, int type2) throws Exception {
		FormattedRelation relation = baseFormattedRelation(this.id, root);
		for (Entry<String, Relation> e : relations.get(type).entrySet()) {
			List<String> rs = e.getValue().relations;
			if (rs.contains(this.id)) {
				
				String pid;
				if (rs.get(0).equals(this.id)) {
					pid = rs.get(1);
				} else {
					pid = rs.get(0);
				}
				if (!relation.getRelations().containsKey(pid)) {
					relation.getRelations().put(pid, baseFormattedRelation(pid, type));
				}
			}
		}
		
		for (Entry<String, FormattedRelation> entry : relation.getRelations().entrySet()) {
			for (Entry<String, Relation> e : relations.get(type2).entrySet()) {
				List<String> rs = e.getValue().relations;
				if (rs.contains(entry.getKey())) {
					
					String pid;
					if (rs.get(0).equals(entry.getKey())) {
						pid = rs.get(1);
					} else {
						pid = rs.get(0);
					}
					
					if (!entry.getValue().getRelations().containsKey(pid)) {
						FormattedRelation formattedRelation = baseFormattedRelation(pid, type2);
						if (formattedRelation != null) {
							entry.getValue().getRelations().put(pid, formattedRelation);
						}
					}
				}
			}
		}
		
		return relation;
	}
	
	private void addRelation(DBObject o, boolean checkDependencies) {
		Gson gson			= Container.get("gson", Gson.class);
		String id			= (String) o.get("_id");
		Relation relation	= gson.fromJson(o.toString(), Relation.class);
		
		if (checkDependencies) {
			if (relation.type != 0) {
				List<String> rs		= relation.relations;
				String pid;
				if (rs.get(0).equals(this.id)) {
					pid = rs.get(1);
				} else {
					pid = rs.get(0);
				}
				
				if (!pids.contains(pid)) {
					pids.add(pid);
				}
			}
		}
		
		if (!relations.containsKey(id)) {
			relations.get(relation.type).put(id, relation);
		}
	}
		
	/**
	 * This method calculates probabilities of apparition for both entities and relations
	 * @param formattedRelation
	 */
	private void calculateProbabilities(FormattedRelation formattedRelation) throws Exception {
		String baseId		= formattedRelation.getId();
		int indirectType	= formattedRelation.getType();
		
		entityProbability(baseId);
		
		// Calculate probabilities for direct relations
		for (FormattedRelation direct : formattedRelation.getRelations().values()) {
			entityProbability(direct.getId());
			
			relationProbability(baseId, direct.getId(), direct.getType());

			// Calculate probabilities for indirect relations
			for (FormattedRelation indirect : direct.getRelations().values()) {
				
				entityProbability(indirect.getId());
				
				// Calculate between direct and indirect
				relationProbability(direct.getId(), indirect.getId(), indirect.getType());
			
				// Calculate between base and indirect
				relationProbability(baseId, indirect.getId(), indirectType);
			}
		}
	}
	
	/**
	 * Calculate the probability of an entity
	 * @param a
	 * @throws Exception
	 */
	private void entityProbability(String a) throws Exception {
		Relation r			= this.relations.get(0).get(a + "-0");
		
		if (r == null) {
			throw new Exception(a);
		}
		
		probabilities.put(a, ((double) r.sentences.size()) / ((double) publications));
	}
	
	/**
	 * Calculate the probability of a relation
	 * @param a
	 * @param b
	 * @param type
	 * @throws Exception
	 */
	private void relationProbability(String a, String b, int type) throws Exception {
		String key 	= String.format("%s-%s", a, b);
		String rKey	= String.format("%s-%s-%s", a, b, type);
		Relation r;
		if (relations.get(type).containsKey(rKey)) {
			r		= relations.get(type).get(rKey);
		} else {
			rKey	= String.format("%s-%s-%s", b, a, type);
			if (!relations.get(type).containsKey(rKey)) {
				throw new Exception(key);
			}
			r = relations.get(type).get(rKey);
		}
		probabilities.put(key, ((double) r.sentences.size()) / ((double) publications));
	}
	
	/**
	 * Calculate scores and return response
	 * @param formattedRelation
	 * @return
	 */
	private Map<String, Map<String, Double>> calculateScores(FormattedRelation formattedRelation) {
		
		String baseId	= formattedRelation.getId();
		
		String key;
		for (FormattedRelation direct : formattedRelation.getRelations().values()) {
			key	= String.format("%s,%s",baseId, direct.getId());
			ds.put(key, calculateD(baseId, direct.getId()));
			
			for (FormattedRelation indirect : direct.getRelations().values()) {
				
				
				key	= String.format("%s,%s", direct.getId(), indirect.getId());
				ds.put(key, calculateD(direct.getId(), indirect.getId()));
				
				key	= String.format("%s,%s", baseId, indirect.getId());
				ds.put(key, calculateD(baseId, indirect.getId()));
			}
		}
		
		// Calculate RS
		for (FormattedRelation direct : formattedRelation.getRelations().values()) {
			for (FormattedRelation indirect : direct.getRelations().values()) {
				key	= String.format("%s,%s", baseId, indirect.getId());
				if (!rs.containsKey(key)) {
					rs.put(key, 1.0);
				}
				rs.put(key, rs.get(key) * calculateR(baseId, direct.getId(), indirect.getId()));
			}
		}
		
		for (Entry<String, Double> e :  rs.entrySet()) {
			rs.put(e.getKey(), 1.0 - e.getValue());
		}
	
		// Calculate scores
		Map<String, Map<String, Double>> scores = new HashMap<String, Map<String, Double>>();
		
		for (FormattedRelation direct : formattedRelation.getRelations().values()) {
			if (!scores.containsKey(direct.getId())) {
				scores.put(direct.getId(), new HashMap<String, Double>());
			}
			
			for (FormattedRelation indirect : direct.getRelations().values()) {
				Double score = calculateScore(baseId,  indirect.getId());
				if (!Double.isInfinite(score) && !Double.isNaN(score)) {
					scores.get(direct.getId()).put(indirect.getId(), score);
				}
			}
		}
		
		return scores;
	}
	
	private Double calculateScore(String e1, String e2) {
		String	key	= String.format("%s,%s", e1, e2);
		if (!rs.containsKey(key) || !ds.containsKey(key)) {
			return Double.NEGATIVE_INFINITY;
		}
		
		return rs.get(key) * (-Math.log10(ds.get(key)));
	}

	private Double calculateR(String e1, String e2, String e3) {
		String	key1	= String.format("%s,%s", e1, e2),
				key2	= String.format("%s,%s", e2, e3);
		
		return 1 - (ds.get(key1) * ds.get(key2));
	}

	private Double calculateD(String e1, String e2) {
		String pKey		= String.format("%s-%s", e1, e2);
		
		double pw = probabilities.get(pKey) / probabilities.get(e2);
		double wp = probabilities.get(pKey) / probabilities.get(e1);
		
		return (pw > wp) ? pw : wp;
	}
}
