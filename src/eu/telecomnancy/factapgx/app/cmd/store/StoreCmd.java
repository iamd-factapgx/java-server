package eu.telecomnancy.factapgx.app.cmd.store;


import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eu.telecomnancy.factapgx.app.cmd.store.exception.InvalidPublicationException;
import eu.telecomnancy.factapgx.app.cmd.store.storage.StorageThread;
import eu.telecomnancy.factapgx.app.model.Publication;
import eu.telecomnancy.factapgx.kernel.cmd.CmdInterface;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;
import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.hadoop.FSManager;


public class StoreCmd implements CmdInterface {

	@Override
	public Object process(String data) throws CmdException {
		Gson gson 			= Container.get("gson", Gson.class);
		FSManager fsManager	= Container.get("fs", FSManager.class);
		
		try {
			Publication publication = gson.fromJson(data, Publication.class);
			if (!publication.valid()) {
				throw new InvalidPublicationException("Publication contains at least an error");
			}
			
			FileSystem fs		= fsManager.getHdfs();
			Path path;
			
			path = new Path(String.format("%s/mapreduce/sentences/%s/%s", fsManager.getRootDir(), publication.getYear(), publication.getId()));
			if (fs.exists(path)) {
				fs.delete(path, true);
			}
			
			path = new Path(String.format("%s/mapreduce/entities/%s/%s", fsManager.getRootDir(), publication.getYear(), publication.getId()));
			if (fs.exists(path)) {
				fs.delete(path, true);
			}
			
			path = new Path(String.format("%s/mapreduce/relations/%s/%s", fsManager.getRootDir(), publication.getYear(), publication.getId()));
			if (fs.exists(path)) {
				fs.delete(path, true);
			}
			
			fs.close();
			return new StorageThread(publication);
		} catch (JsonSyntaxException | IOException | URISyntaxException e) {
			throw new InvalidPublicationException("Invalid JSON", e);
		}
	}
}
