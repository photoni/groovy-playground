package net

import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method;
import groovyx.net.http.ParserRegistry
import groovyx.net.http.Method.*

import org.apache.http.client.fluent.Request

import au.com.bytecode.opencsv.CSVReader;
/**
 * Performs Http basic CLIENT operations
 * @author filippo
 *
 */
@Slf4j
class HttpClientOps {


	/**
	 * Simply get the flat url response
	 * @param url
	 * @return
	 */
	String getHttpSimple(String url){
		return Request.Get(url)
		.connectTimeout(1000)
		.socketTimeout(1000)
		.execute().returnContent().asString()
	}

	/**
	 * Get content from an url with a specific path and query parameter
	 * @param url
	 * @return json
	 */
	def getJson(String url,Map<String,Object> params){
		def http = new HTTPBuilder(url);
		/*  path: 'weather', query: [q: 'London']  */
		http.get( params) { resp, json ->
			log.trace("Http Response: {}",resp.dump())
			return json
		}
	}

	/**
	 * Get csv content from an url with a specific path and query parameter.<br/>
	 * Handles {@link ContentType}
	 * @param url
	 * @return csv parsed in a list of objects
	 */
	def List<Map<String,?>> getCsv(String url,Map<String,Object> params){
		def http = new HTTPBuilder(url);
		http.parser.'text/csv' = { resp ->
			return new CSVReader(new InputStreamReader( resp.entity.content,
			ParserRegistry.getCharset( resp ) ) )
		}
		http.request( Method.GET, 'text/csv' ) { req ->
			uri.query = params
			headers.Accept = 'text/csv'
			response.success = { resp, reader ->
				return reader.readAll();
			}
		}
	}

	/**
	 * Get csv content from an url with a specific path and query parameter.<br/>
	 * Handles {@link ContentType}
	 * @param url
	 * @return csv not parsed.
	 */
	def byte[] getCsvAsBinary(String url,Map<String,Object> params){
		def http = new HTTPBuilder(url);
		http.request( Method.GET, 'text/csv' ) { req ->
			uri.query = params
			headers.Accept = 'text/csv'
			response.success = { resp->
				return resp.entity.content.bytes;
			}
		}
	}
}
