package eu.telecomnancy.factapgx.app.cmd.find.exception;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public class FindCmdException extends CmdException {
	
	private static final long serialVersionUID = 1L;

	public FindCmdException(String message) {
		super(message);
	}
	
	public FindCmdException(String message, Throwable previous) {
		super(message, previous);
	}

	@Override
	public int getCode() {
		return 201;
	}

}
