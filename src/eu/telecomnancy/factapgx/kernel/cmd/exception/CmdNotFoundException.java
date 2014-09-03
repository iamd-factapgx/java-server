package eu.telecomnancy.factapgx.kernel.cmd.exception;

public class CmdNotFoundException extends CmdException {

	private static final long serialVersionUID = -4356476945195066411L;

	public CmdNotFoundException(String message) {
		super(message);
	}

	@Override
	public int getCode() {
		return 101;
	}
}
