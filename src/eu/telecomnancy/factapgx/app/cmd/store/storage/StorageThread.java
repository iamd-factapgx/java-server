package eu.telecomnancy.factapgx.app.cmd.store.storage;

import java.io.PrintWriter;
import java.net.Socket;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.gson.Gson;
import eu.telecomnancy.factapgx.app.model.Publication;
import eu.telecomnancy.factapgx.kernel.Kernel;
import eu.telecomnancy.factapgx.kernel.conf.MapredConf;
import eu.telecomnancy.factapgx.vendor.dependencyinjection.Container;
import eu.telecomnancy.factapgx.vendor.hadoop.FSManager;



public class StorageThread extends Thread {
	
	private Publication publication;
	
	public StorageThread(Publication publication) {
		this.publication = publication;
		this.setName("store_" + publication.getId());
	}
	
	public void run()
	{
		synchronized (this) {
			try {
				Gson gson					= Container.get("gson", Gson.class);
				FSManager fsManager			= Container.get("fs", FSManager.class);
				
				FileSystem fs				= fsManager.getHdfs();
				
				Path path					= new Path(String.format("%s/%s", fsManager.getPublicationsYearStorageDirectory(publication.getYear()), publication.getId())); 
				
				// Store publication in HDFS
				if (fs.exists(path)) {
					fs.delete(path, true);
				}
				PrintWriter writer;
				
				FSDataOutputStream os		= fs.create(path);
				writer						= new PrintWriter(os);
				writer.println(gson.toJson(publication));
				writer.flush();
				writer.close();
				
				fs.close();
				
				// Start mapreduce processes
				MapredConf mapredConf	= Kernel.conf().mapred();
				Socket socket			= new Socket(mapredConf.getAddress(), mapredConf.getPort());
				
				writer					= new PrintWriter(socket.getOutputStream());
				writer.println(String.format("%s %s", publication.getYear(), publication.getId()));
				writer.flush();
				
				writer.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
