package lb;

import com.sun.net.httpserver.HttpServer;
import lb.core.BalancerState;
import lb.handler.HealthChecker;
import lb.handler.MetricsHandler;
import lb.handler.ProxyHandler;
import lb.strategy.BalancerStrategy;
import lb.strategy.WeightedLeastConnectionsStrategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.Executors;

public class LoadBalancer {

    public static void main(String[] args) throws IOException {

        BalancerState state =
                new BalancerState(
                        List.of(
                                "http://api-gateway:9000"
                        )
                );

        HttpClient client = HttpClient.newHttpClient();

        BalancerStrategy strategy = new WeightedLeastConnectionsStrategy(state);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new ProxyHandler(state, client, strategy));
        server.createContext("/metrics", new MetricsHandler(state));

        server.setExecutor(Executors.newCachedThreadPool());

        server.start();

        new HealthChecker(state, client).start();

        System.out.println("Load Balancer started on port 8080");
    }
}