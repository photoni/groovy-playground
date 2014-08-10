package rest

import org.vertx.java.core.Handler
import org.vertx.java.core.Vertx
import org.vertx.java.core.VertxFactory
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.java.platform.Verticle

/**
 * Rest container implemented with Vert.x http server
 * @author filippo
 *
 */
class VertXRestContainer extends Verticle{
	Vertx vertx = VertxFactory.newVertx();

	public void start() {
		
		vertx.createHttpServer().requestHandler { req ->
			req.response().headers().set("Content-Type", "text/plain");
			req.response().end("Pippo World");
		}.listen(8080)

	}

	public static void main(String[] list) throws Exception {
		VertXRestContainer vrc=new VertXRestContainer();
		vrc.start();
		System.in.read();
	}
}
