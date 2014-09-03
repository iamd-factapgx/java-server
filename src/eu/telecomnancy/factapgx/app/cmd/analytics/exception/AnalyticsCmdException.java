package eu.telecomnancy.factapgx.app.cmd.analytics.exception;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public class AnalyticsCmdException extends CmdException {
	private static final long serialVersionUID = 5468200798767697376L;

	public AnalyticsCmdException(String message) {
		super(message);
	}
	
	public AnalyticsCmdException(String message, Throwable previous) {
		super(message, previous);
	}

	@Override
	public int getCode() {
		return 401;
	}

}
