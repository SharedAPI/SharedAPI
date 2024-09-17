package fr.traqueur.shared.api;

import com.google.gson.Gson;
import fr.traqueur.shared.api.domain.Schema;
import fr.traqueur.shared.api.requests.body.AuthBody;
import fr.traqueur.shared.api.requests.responses.TokenResponse;
import fr.traqueur.shared.api.requests.EndPoints;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SharedClient {

    private final SharedAPI api;
    private final Gson gson;
    private final UUID plugin;
    private final UUID server;
    private final HttpClient webClient;
    private String token;

    protected SharedClient(SharedAPI api, Gson gson, UUID plugin, UUID server) throws ExecutionException, InterruptedException {
        this.api = api;
        this.gson = gson;
        this.plugin = plugin;
        this.server = server;
        this.webClient = this.buildClient();
        this.getToken(server, plugin)
                .thenAccept(tokenResponse -> this.token = tokenResponse.token())
                .get();
        if (this.api.isDebug()) {
            this.api.getLogger().info("Successfully connected to the API.");
        }
    }

    private HttpClient buildClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private CompletableFuture<TokenResponse> getToken(UUID server, UUID plugin) {
        try (var client = this.webClient) {

            String body = this.gson.toJson(new AuthBody(server, plugin), AuthBody.class);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(SharedAPI.API_URL.resolve(EndPoints.AUTH))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            return response.thenApply(responseInner -> this.gson.fromJson(responseInner.body(), TokenResponse.class));
        }
    }

    protected void sendSchema(Schema schema) {
        String body = this.gson.toJson(schema, Schema.class);

        var builderRequest =
                this.buildRequest(EndPoints.SCHEMA.match("plugin_uuid", this.plugin.toString()));

        this.sendRequest(builderRequest
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()).thenAccept(response -> {
            if(this.api.isDebug()) {
                this.api.getLogger().info("The schema has been loaded.");
            }
        });
    }

    public CompletableFuture<HttpResponse<String>> sendRequest(HttpRequest request) {
        try (var client = this.webClient) {
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    public HttpRequest.Builder buildRequest(EndPoints endpoint) {
        if(endpoint.getPath().contains("{") || endpoint.getPath().contains("}")) {
            throw new IllegalArgumentException("The path " + endpoint.getPath() + " contains a key.");
        }

        return HttpRequest.newBuilder()
                .uri(SharedAPI.API_URL.resolve(endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.token);
    }

    public HttpClient getWebClient() {
        return this.webClient;
    }

    public String getToken() {
        return token;
    }
}
