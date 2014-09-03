package eu.telecomnancy.factapgx.app.cmd.analytics;


import eu.telecomnancy.factapgx.app.cmd.analytics.analytics.RelationLoader;
import eu.telecomnancy.factapgx.app.cmd.analytics.exception.AnalyticsCmdException;
import eu.telecomnancy.factapgx.kernel.cmd.CmdInterface;
import eu.telecomnancy.factapgx.kernel.cmd.exception.CmdException;

public class AnalyticsCmd implements CmdInterface {

	@Override
	public Object process(String data) throws CmdException {
		try {
			String[] parts		= data.split(" ");
			RelationLoader relations	= new RelationLoader(parts[0], parts[1]);
			
			return relations.fetch();
		} catch (Exception e) {
			throw new AnalyticsCmdException(e.getMessage(), e);
		}
	}
}
