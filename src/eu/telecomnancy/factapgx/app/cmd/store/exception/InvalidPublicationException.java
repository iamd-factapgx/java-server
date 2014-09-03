package eu.telecomnancy.factapgx.app.cmd.store.exception;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public class InvalidPublicationException extends CmdException {
	
	private static final long serialVersionUID = 3034472774570979566L;

	public InvalidPublicationException(String message) {
		super(message);
	}
	
	public InvalidPublicationException(String message, Throwable previous) {
		super(message, previous);
	}

	@Override
	public int getCode() {
		return 302;
	}

}
