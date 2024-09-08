package fr.traqueur.shared.api;

import com.google.gson.Gson;
import fr.traqueur.shared.api.domain.AuthBody;
import fr.traqueur.shared.api.requests.EndPoints;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SharedClient {

    private Gson gson;
    private final HttpClient webClient;
    private String token;

    protected SharedClient(Gson gson, UUID plugin, UUID server) throws ExecutionException, InterruptedException {
        this.gson = gson;
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

            String body = this.gson.toJson(new AuthBody(server, plugin), AuthBody.class);

            var request = HttpRequest.newBuilder()
                    .uri(SharedAPI.API_URL.resolve(EndPoints.AUTH))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
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
