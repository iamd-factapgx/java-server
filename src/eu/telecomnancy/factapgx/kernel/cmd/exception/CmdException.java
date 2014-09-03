package eu.telecomnancy.factapgx.kernel.cmd.exception;

abstract public class CmdException extends Exception {
	
	private static final long serialVersionUID = 4047260350894706400L;
	
	public CmdException(String message) {
		super(message);
	}
	
	public CmdException(String message, Throwable previous) {
		super(message, previous);
	}
	
	abstract public int getCode();
}
