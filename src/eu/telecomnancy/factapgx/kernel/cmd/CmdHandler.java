package eu.telecomnancy.factapgx.kernel.cmd;

import java.util.HashMap;
import java.util.Map;

import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdNotFoundException;


/**
 * This class handles commands
 */
public class CmdHandler {
	
	private Map<String, CmdInterface> cmds;
	
	public CmdHandler() {
		cmds = new HashMap<String, CmdInterface>();
	}
	
	/**
	 * Register a new command
	 * @param name
	 * @param cmd
	 */
	public void register(String name, CmdInterface cmd) {
		cmds.put(name,  cmd);
	}
	
	/**
	 * Process a command using its name
	 * @param name
	 * @param data
	 * @return
	 * @throws CmdException
	 */
	public Object process(String name, String data) throws CmdException {
		if (cmds.containsKey(name)) {
			return cmds.get(name).process(data);
		}
		throw new CmdNotFoundException(String.format("Impossible to process command : \"%s\"", name));
	}
}
