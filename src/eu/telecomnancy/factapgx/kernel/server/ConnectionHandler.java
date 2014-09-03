package eu.telecomnancy.factapgx.kernel.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import eu.telecomnancy.factapgx.kernel.cmd.CmdHandler;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;
import eu.telecomnancy.factapgx.vendor.log.Logger;


class ConnectionHandler extends Thread {
	private Socket socket;
	private CmdHandler cmdHandler;

	public ConnectionHandler(Socket socket, CmdHandler cmdHandler) {
		this.socket		= socket;
		this.cmdHandler	= cmdHandler;
	}
	
	public void run() {
		try {
			socket.setKeepAlive(true);
			socket.setSoTimeout(0);
			BufferedReader reader	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer	= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String line				= reader.readLine();
			
			Object ret 				= processCmd(line.trim());
			if (ret instanceof Thread) {
				Thread thread = (Thread) ret;
				
				writer.write("{\"ok\": true, \"data\": []}\n");
				writer.flush();
				
				reader.close();
				writer.close();
				socket.close();
				
				thread.start();
				synchronized (thread) {
					try {
						thread.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			} else {
				writer.write(((String) ret) + "\n");
				writer.flush();
				reader.close();
				writer.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object processCmd(String cmd) {
		try {
			int pos;
			String name, data;
			if (-1 == (pos = cmd.indexOf(' '))) {
				name 	= cmd.trim();
				data	= "";
			} else {
				name	= cmd.substring(0, pos).trim();
				data	= cmd.substring(pos).trim();
			}
			return cmdHandler.process(name, data);
		} catch (CmdException e) {
			Logger.err(String.format("cmd.Error\t%s", e.getMessage()));
			e.printStackTrace();
			return String.format("{\"ok\": false, \"code\": %s, \"error\": \"%s\"}", e.getCode(), e.getMessage());
		}
	}
}
