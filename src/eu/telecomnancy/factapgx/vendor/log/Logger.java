package eu.telecomnancy.factapgx.vendor.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import eu.telecomnancy.factapgx.kernel.conf.Conf;


public class Logger {
	
	private static PrintStream out;
	private static PrintStream err;

	public static void init(Conf conf) throws IOException {
		out = System.out;
		err = System.err;
		
		File logsDir = new File(conf.getLogs());
		if (!logsDir.exists()) {
			logsDir.mkdirs();
		}
		
		if (!conf.isDebug()) {
			
			File logsFile = new File(conf.getLogs() + "/logs.logs");
			System.setOut(new PrintStream(logsFile));
			
			File errorsFile = new File(conf.getLogs() + "/errors.logs");
			System.setErr(new PrintStream(errorsFile));
		}
	}
	
	private static void log(PrintStream stream, String log) {
		stream.println(String.format("[%s] %s", new Date(), log));
	}
	
	public static void log(String log) {
		log(System.out, log);
	}
	
	public static void err(String log) {
		log(System.err, log);
	}
	
	public static void consoleLog(String log) {
		log(out, log);
	}
	
	public static void consoleErr(String log) {
		log(err, log);
	}
}
