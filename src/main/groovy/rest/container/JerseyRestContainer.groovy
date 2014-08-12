package rest.container;

import groovy.util.logging.Slf4j

import javax.ws.rs.core.UriBuilder

import com.sun.jersey.api.container.httpserver.HttpServerFactory
import com.sun.jersey.api.core.PackagesResourceConfig
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer

@Slf4j
@Singleton(strict=false)
//TODO upgrade to Jersey 2.11
class JerseyRestContainer {

	HttpServer createHttpServer() throws IOException {
		ResourceConfig resourceConfig = new PackagesResourceConfig("rest")
		// This tutorial required and then enable below line: http://w.crunchify.com/cors-filters
		//crunchifyResourceConfig.getContainerResponseFilters().add(CrunchifyCORSFilter.class);
		return HttpServerFactory.create(getURI(), resourceConfig)
	}

	URI getURI() {
		return UriBuilder.fromUri("http://" + getHostName() + "/").port(8080).build()
	}

	String getHostName() {
		String hostName = "localhost"
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName()
		} catch (UnknownHostException e) {
			log.error(e.getMessage(), e)
		}
		return hostName
	}
	
	void start(){
		log.debug("Starting Embedded Jersey HTTPServer...\n")
		HttpServer server = createHttpServer()
		
		server.start()
		log.debug(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getURI()))
		log.debug("Started Crunchify's Embedded Jersey HTTPServer Successfully !!!")
		
	}
	public static void main(String[] args) throws IOException {
		JerseyRestContainer jerseyServer=JerseyRestContainer.instance
		jerseyServer.start()
	}
}
