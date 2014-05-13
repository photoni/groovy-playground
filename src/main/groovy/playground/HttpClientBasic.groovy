package playground

import groovyx.net.http.HTTPBuilder
import net.sf.json.JSONObject

import org.apache.http.client.fluent.Request

class HttpClientBasic {
	static void main(def args) {
		def hcb=new HttpClientBasic()
		String resp=hcb.getHttpSimple("http://google.com")
		println resp
		def json=hcb.getJson("http://api.openweathermap.org/data/2.5/")
		println "It is currently ${json.weather} in London."
       
	}

	String getHttpSimple(String url){
		return Request.Get(url)
		.connectTimeout(1000)
		.socketTimeout(1000)
		.execute().returnContent().asString()
	}

	def getJson(String url){
		def http = new HTTPBuilder(url)
		http.get( path: 'weather', query: [q: 'London'] ) { resp, json ->
			println resp			
			return json
		}
	}
}
