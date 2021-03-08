package tt.exporter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Start {

	public static void main(String[] args) {
		
		int threadCounter = 0;
		JsonObject clientConfig = null;
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("config.json")));
		br.lines().forEach(line -> {
			builder.append(line);
		});
		clientConfig = new JsonObject(builder.toString());

        final Vertx vertx = Vertx.vertx();
		Server server = new Server(vertx, clientConfig.getString("scrape route"), clientConfig.getInteger("port"));
		server.startServer();
		
		for (Object elem : clientConfig.getJsonArray("clients")) {
			JsonObject threadSettings = (JsonObject) elem;
			Client client = new Client(vertx, 
									   threadSettings.getString("address"), 
									   threadSettings.getString("endpoint"), 
									   threadSettings.getInteger("target port"), 
									   threadSettings.getInteger("probe intervall millis"), 
									   threadCounter++);
			client.start();
		}
        
		
	}

}
