package eu.telecomnancy.factapgx.app.cmd.find.find;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.elasticsearch.ElasticsearchManager;


public class ElasticsearchResults {
	
	private String query;

	public ElasticsearchResults(String query) {
		this.query = query;
	}
	
	public Map<String, Integer> getPids() throws ClientProtocolException, IOException {
		Gson gson					= Container.get("gson", Gson.class);
		ElasticsearchManager es		= Container.get("elasticsearch", ElasticsearchManager.class);
		
		
		Map<?, ?> hits;
		List<?> hs;
		
		// Get results
		String queryResult	= es.query("publications", query, "disease", 5);
		hits				= gson.fromJson(queryResult, HashMap.class);
		hs					= (List<?>) ((Map<?, ?>) hits.get("hits")).get("hits");
		
		Map<String, Integer> pids = new HashMap<String ,Integer>();
		for (Object h : hs) {
			Map<?, ?> hit		= (Map<?, ?>) ((Map<?, ?>) h).get("_source");
			String pid	= (String) hit.get("id");
			if (!pids.containsKey(pid)) {
				int year = (int) Math.floor((Double) hit.get("year"));
				pids.put(pid, year);
			}
		}
		return pids;
	}
}
