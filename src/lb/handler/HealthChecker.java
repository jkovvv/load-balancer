package lb.handler;
import lb.core.BalancerState;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HealthChecker {

    private final BalancerState state;
    private final HttpClient client;

    public HealthChecker(BalancerState state, HttpClient client) {
        this.state = state;
        this.client = client;
    }

    public void start() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            while (true) {
                for (int i = 0; i < state.servers.length; i++) {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(state.servers[i] + "/health"))
                                .timeout(Duration.ofSeconds(2))
                                .GET()
                                .build();

                        HttpResponse<String> response =
                                client.send(request, HttpResponse.BodyHandlers.ofString());

                        state.alive[i].set(response.statusCode() == 200);

                    } catch (Exception e) {
                        state.alive[i].set(false);
                    }
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
            }
        });
    }
}