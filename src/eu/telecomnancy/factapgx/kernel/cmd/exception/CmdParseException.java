package eu.telecomnancy.factapgx.kernel.cmd.exception;

public class CmdParseException extends CmdException {
	
	private static final long serialVersionUID = -5529357286015155953L;

	public CmdParseException(String message) {
		super(message);
	}

	@Override
	public int getCode() {
		return 102;
	}
}
