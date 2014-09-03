package eu.telecomnancy.factapgx.vendor.elasticsearch;

import java.io.IOException;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import eu.telecomnancy.factapgx.kernel.conf.Conf;
import eu.telecomnancy.factapgx.kernel.conf.EsConf;


public class ElasticsearchManager {
	
	private EsConf conf;

	public ElasticsearchManager(Conf conf) {
		this.conf	= conf.es();
	}
	
	public String put(String type, String id, String json) throws ClientProtocolException, IOException {
		StringEntity entity = new StringEntity(json, ContentType.create("application/json", Consts.UTF_8));
		entity.setChunked(true);
		
		return Request
					.Put(URIUtil.encodeQuery(String.format("%s/%s/%s", conf.getUrl(), type, id)))
					.body(entity)
					.execute()
					.returnContent()
					.asString();
	}
	
	public String count(String type, String query, String df) throws ClientProtocolException, IOException {
		return Request
				.Get(URIUtil.encodeQuery(String.format("%s/%s/_search?q=%s&df=%s&search_type=count", conf.getUrl(), type, query, df)))
				.execute()
				.returnContent()
				.asString();
	}
	
	public String query(String type, String query, String df, int size) throws ClientProtocolException, IOException {
		return Request
				.Get(URIUtil.encodeQuery(String.format("%s/%s/_search?q=%s&df=%s&size=%s", conf.getUrl(), type, query, df, size)))
				.execute()
				.returnContent()
				.asString();
	}
	
	public String get(String type, String id) throws ClientProtocolException, IOException {
		return Request
				.Get(URIUtil.encodeQuery(String.format("%s/%s/%s", conf.getUrl(), type, id)))
				.execute()
				.returnContent()
				.asString();
	}
}
