package fr.traqueur.shared.api;

import fr.traqueur.shared.api.requests.EndPoints;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SharedClient {

    private final HttpClient webClient;
    private String token;

    protected SharedClient(UUID plugin, UUID server) throws ExecutionException, InterruptedException {
        this.webClient = this.buildClient();
        this.getToken(server, plugin)
                .thenAccept(token -> this.token = token)
                .get();
    }

    private HttpClient buildClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private CompletableFuture<String> getToken(UUID server, UUID plugin) {
        try (var client = this.webClient) {
            var request = HttpRequest.newBuilder()
                    .uri(SharedAPI.API_URL.resolve(EndPoints.AUTH
                                    .get("server", server.toString())
                                    .get("plugin", plugin.toString())))
                    .GET()
                    .build();
            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            return response.thenApply(HttpResponse::body);
        }
    }

    public HttpRequest.Builder request(EndPoints endpoint) {
        return HttpRequest.newBuilder()
                .uri(SharedAPI.API_URL.resolve(endpoint))
                .header("Authorization", "Bearer " + this.token);
    }

    public HttpClient getWebClient() {
        return this.webClient;
    }

}
