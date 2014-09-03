package eu.telecomnancy.factapgx.kernel.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import eu.telecomnancy.factapgx.app.cmd.analytics.AnalyticsCmd;
import eu.telecomnancy.factapgx.app.cmd.diseases.DiseasesCmd;
import eu.telecomnancy.factapgx.app.cmd.find.FindCmd;
import eu.telecomnancy.factapgx.app.cmd.store.StoreCmd;
import eu.telecomnancy.factapgx.kernel.cmd.CmdHandler;
import eu.telecomnancy.factapgx.vendor.log.Logger;

public class Server {
	
	private boolean running;
	private ServerSocket server;
	private int port;
	private CmdHandler cmdHandler;
	
	public Server(int port) {
		this.port		= port;
		
		// Setup Command handler
		cmdHandler		= new CmdHandler();
		cmdHandler.register("STORE", new StoreCmd());
		cmdHandler.register("FIND", new FindCmd());
		cmdHandler.register("ANALYTICS", new AnalyticsCmd());
		cmdHandler.register("DISEASES", new DiseasesCmd());
	}
	
	public void start() throws IOException {
		server		= new ServerSocket(port);
		running		= true;
		
		Logger.consoleLog(String.format("Server running at %s:%s", server.getInetAddress().getHostAddress(), port));
		
		while (running) {
			Socket connection = server.accept();
			(new ConnectionHandler(connection, cmdHandler)).start();
		}
	}
	
	public void stop() {
		try {
			server.close();
			running = false;
			
			Logger.consoleLog(String.format("Server stopped"));
		} catch(IOException e) { e.printStackTrace(); }
	}
}
