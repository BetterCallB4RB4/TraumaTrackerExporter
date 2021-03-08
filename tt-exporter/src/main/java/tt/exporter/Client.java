package tt.exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import tt.utils.Pair;

public class Client extends Thread{

	private static Counter totalRequest; 
	private static Counter failureRequest;
	private static Gauge serviceLatency; 
		
	private final Vertx vertx;
	private WebClient client;
	
	private final int port;
	private final String endpoint;
	private final String address;
	private final int timeRequestMillis;
	private final int threadCounter;
	
	private String[] keyLabel;
	private String[] valueLabel;
	
	public Client(final Vertx vertx, final String address, final String endpoint, final int port, final int timeRequestMillis, final int threadCounter) {
		this.vertx = vertx;
		this.client = WebClient.create(this.vertx);
		
		this.address = address;
		this.endpoint = endpoint;
		this.port = port;
		this.timeRequestMillis = timeRequestMillis;
		this.threadCounter = threadCounter;
		
		this.buildMetrics();
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("sending request");
				Client.totalRequest.labels(this.valueLabel).inc(); 
				long start = System.nanoTime();				
				client.get(this.port, this.address, this.endpoint).send(response -> {
					long elapsedTime = System.nanoTime() - start;
					Client.serviceLatency.labels(this.valueLabel).set(elapsedTime);
					if (response.succeeded()) {
						//DEBUG
						//HttpResponse<Buffer> httpResponse = response.result();
						//System.out.println("GET Response : " + httpResponse.bodyAsString());
						System.out.println("http status code : " + response.result().statusCode());
					} else {
						failureRequest.labels(this.address, this.endpoint).inc();
					}
				});
				System.out.println("total request : " + totalRequest.labels(this.valueLabel).get());
				System.out.println("failure request : " + failureRequest.labels(this.valueLabel).get());
				System.out.println("service latency : " + serviceLatency.labels(this.valueLabel).get());
				Thread.sleep(this.timeRequestMillis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buildMetrics() {
		this.buildLabels();
		Client.totalRequest = Counter.build()
				                     .name("exporter_" + this.threadCounter + "_http_total_request")
				                     .help("the number of total requests")
				                     .labelNames(this.keyLabel)
				                     .register();
		
		Client.failureRequest = Counter.build()
				                       .name("exporter_" + this.threadCounter + "_http_failure_request")
				                       .help("the number of requests that returned a failed outcome")
				                       .labelNames(this.keyLabel)
				                       .register();
		
		Client.serviceLatency = Gauge.build()
				                     .name("exporter_" + this.threadCounter + "_network_service_latency")
				                     .help("how long does the service take to satisfy the request")
				                     .labelNames(this.keyLabel)
				                     .register();
	}
	
	private void buildLabels() {
		List<Pair<String, String>> labels = new ArrayList<>();
		
		//add new key value label here
		labels.add(new Pair<>("service", this.address));
		labels.add(new Pair<>("endpoint", this.endpoint));
		
		this.keyLabel = labels.stream().map(i -> i.getX()).collect(Collectors.toList()).toArray(new String[0]);
		this.valueLabel = labels.stream().map(i -> i.getY()).collect(Collectors.toList()).toArray(new String[0]);
	}
	
}
