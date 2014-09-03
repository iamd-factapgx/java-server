package eu.telecomnancy.factapgx.kernel.cmd;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public interface CmdInterface {
	
	/**
	 * Process command
	 * @return String|Thread
	 * @throws CmdException
	 */
	public Object process(String data) throws CmdException;
}
