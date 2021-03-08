package tt.exporter;

import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Server {

	private final String route;
	private final int port;
	private final Vertx vertx;
	
	public Server(final Vertx vertx, final String route, final int port) {
		this.route = route;
		this.port = port;
		this.vertx = vertx;
	}
	
	private void serverSetup() {
		final Router router = Router.router(this.vertx);
	    router.route(this.route).handler(new MetricsHandler());
	    vertx.createHttpServer().requestHandler(router::accept).listen(this.port);
	}
	
	public void startServer() {
		this.serverSetup();
	}
	
}
