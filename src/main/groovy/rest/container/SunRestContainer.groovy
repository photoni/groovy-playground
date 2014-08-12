package rest.container

import java.util.concurrent.Executors

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer

class SunRestContainer implements HttpHandler {
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = httpExchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "application/json");
			OutputStream responseBody = httpExchange.getResponseBody();

			final String query = httpExchange.getRequestURI().getRawQuery();
			final String path = httpExchange.getRequestURI().getPath();
			
			String response=path+"-"+ query;
			
			httpExchange.sendResponseHeaders(200, 0);
			responseBody.write(response.bytes);
			responseBody.close();
		}
	}



	//	public static void main(String[] list) throws Exception {
	//		Container container = new RestAPI();
	//		Server server = new ContainerServer(container);
	//		Connection connection = new SocketConnection(server);
	//		SocketAddress address = new InetSocketAddress(8080);
	//
	//		connection.connect(address);
	//	 }

	public static void main(String[] list) throws Exception {
		InetSocketAddress addr = new InetSocketAddress(8080)
		HttpServer httpServer = com.sun.net.httpserver.HttpServer.create(addr, 0)
		httpServer.with {
			createContext('/analysis', new SunRestContainer())
			setExecutor(Executors.newCachedThreadPool())
			start()
		}
	}


}
