package eu.telecomnancy.factapgx.app.cmd.diseases.exception;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public class DiseasesCmdException extends CmdException {
	private static final long serialVersionUID = 1140080088947192562L;
	
	public DiseasesCmdException(String message, Throwable previous) {
		super(message, previous);
	}

	@Override
	public int getCode() {
		return 501;
	}

}
