package lb.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lb.core.BalancerState;

import java.io.IOException;

public class MetricsHandler implements HttpHandler {

    private final BalancerState state;

    public MetricsHandler(BalancerState state) {
        this.state = state;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        StringBuilder json = new StringBuilder("{");

        for (int i = 0; i < state.servers.length; i++) {

            long avgLatency = 0;

            if (state.count[i].get() > 0) {
                avgLatency =
                        (state.latency[i].get() / state.count[i].get()) / 1_000_000;
            }

            json.append("\"").append(3001 + i).append("\":{")
                    .append("\"active\":").append(state.active[i].get()).append(",")
                    .append("\"alive\":").append(state.alive[i].get()).append(",")
                    .append("\"avgLatencyMs\":").append(avgLatency)
                    .append("}");

            if (i < state.servers.length - 1) {
                json.append(",");
            }
        }

        json.append("}");

        byte[] data = json.toString().getBytes();

        exchange.sendResponseHeaders(200, data.length);
        exchange.getResponseBody().write(data);
        exchange.getResponseBody().close();
    }
}