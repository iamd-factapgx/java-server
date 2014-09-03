package eu.telecomnancy.factapgx.app.cmd.find;

import java.util.Map;

import eu.telecomnancy.factapgx.app.cmd.find.exception.FindCmdException;
import eu.telecomnancy.factapgx.app.cmd.find.find.ElasticsearchResults;
import eu.telecomnancy.factapgx.app.cmd.find.find.HadoopResults;
import eu.telecomnancy.factapgx.kernel.cmd.CmdInterface;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;


public class FindCmd implements CmdInterface {

	@Override
	public Object process(String data) throws CmdException {
		try {
			Map<String, Integer>  pids	= (new ElasticsearchResults(data)).getPids();
			String results				= (new HadoopResults(pids)).getResults();
			return String.format("{\"ok\": true, \"hits\": %s }", results);
		} catch (Exception e) {
			if (e instanceof CmdException) {
				throw (CmdException) e;
			} else {
				throw new FindCmdException("Invalid query", e);
			}
		}
	}

}
