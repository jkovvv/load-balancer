package lb;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.Random;

public class SimpleServer {

    public static void main(String[] args) throws IOException {

        int serverId = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);

        HttpServer server = HttpServer.create(
                new InetSocketAddress(port),
                0
        );

        server.createContext("/", new RootHandler(serverId));
        server.createContext("/health", new HealthHandler());

        server.setExecutor(Executors.newFixedThreadPool(50));

        server.start();

        System.out.println(
                "Server " + serverId + " started on port " + port
        );
    }

    static class RootHandler implements HttpHandler {

        private final int serverId;
        private static final Random random = new Random();

        public RootHandler(int serverId) {
            this.serverId = serverId;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            simulateLatency();

            String response = "Hello from Server " + serverId;

            send(exchange, 200, response);
        }

        private void simulateLatency() {

            try {

                switch (serverId) {

                    case 1 -> Thread.sleep(120 + random.nextInt(50));

                    case 2 -> Thread.sleep(80 + random.nextInt(50));

                    case 3 -> Thread.sleep(50 + random.nextInt(50));
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class HealthHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            send(exchange, 200, "OK");
        }
    }

    static void send(
            HttpExchange exchange,
            int statusCode,
            String body
    ) throws IOException {

        byte[] data = body.getBytes();

        exchange.sendResponseHeaders(statusCode, data.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(data);
        }
    }
}