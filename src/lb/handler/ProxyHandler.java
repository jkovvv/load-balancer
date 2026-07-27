package lb.handler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lb.core.BalancerState;
import lb.strategy.BalancerStrategy;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.http.HttpClient;

public class ProxyHandler implements HttpHandler {

    private final BalancerState state;
    private final BalancerStrategy strategy;
    private final HttpClient client;

    public ProxyHandler(BalancerState state, HttpClient client, BalancerStrategy strategy) {
        this.state = state;
        this.client = client;
        this.strategy = strategy;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        int index = strategy.chooseServer();

        if (index == -1) {
            send(exchange, 503, "No servers available");
            return;
        }

        String target = state.servers.get(index);

        state.active[index].incrementAndGet();

        long start = System.nanoTime();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(target + exchange.getRequestURI()))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long duration = System.nanoTime() - start;

            state.latency[index].addAndGet(duration);
            state.count[index].incrementAndGet();

            send(exchange, response.statusCode(), response.body());

        } catch (Exception e) {
            send(exchange, 502, "Bad Gateway");

        } finally {
            state.active[index].decrementAndGet();
        }
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        ex.sendResponseHeaders(code, body.length());
        ex.getResponseBody().write(body.getBytes());
        ex.getResponseBody().close();
    }
}