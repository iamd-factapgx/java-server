package eu.telecomnancy.factapgx.app.cmd.find.find;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.hadoop.FSManager;

public class HadoopResults {
	
	private Map<String, Integer>  pids;
	
	public HadoopResults(Map<String, Integer>  pids) {
		this.pids = pids;
	}
	
	public String getResults() throws Exception {
		FSManager fsManager	= Container.get("fs", FSManager.class);
		FileSystem fs		= fsManager.getHdfs();
		
		String ret			= "[";
		int i = 0;
		for (Entry<String, Integer> e : pids.entrySet()) {
			if (i > 0) {
				ret += ", ";
			}
			
			
			String file = fsManager.getPublicationsYearStorageDirectory(e.getValue()) + "/" + e.getKey();
			Path path	= new Path(file);
			if (!fs.exists(path)) {
				continue;
			}
			
			String out = "", line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(path)));
			while (null != (line = reader.readLine())) {
				if (line.trim().isEmpty()) {
					continue;
				}
				out += line;
			}
			
			ret += out;
			
			reader.close();
			i++;
		}
		
		fs.close();
		return ret + "]";
	}
}
