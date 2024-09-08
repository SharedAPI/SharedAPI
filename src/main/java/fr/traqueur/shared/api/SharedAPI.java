package fr.traqueur.shared.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.shared.api.domain.AuthBody;
import fr.traqueur.shared.api.updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SharedAPI {

    public static final SharedURL API_URL = new SharedURL("http://localhost:8000/api");

    private final Gson gson;
    private final Logger logger;
    private final SharedClient client;

    public SharedAPI(@Nullable JavaPlugin instance, UUID plugin, UUID server) {
        this.logger = instance == null ? Logger.getLogger("SharedAPI") : instance.getLogger();
        this.gson = this.buildGson();
        try {
            this.client = new SharedClient(this.gson, plugin, server);
        } catch (ExecutionException | InterruptedException e) {
            this.logger.severe("An error occurred while trying to connect to the API.");
            throw new RuntimeException(e);
        }
        Updater.checkUpdates();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    public Logger getLogger() {
        return logger;
    }

    public Gson getGson() {
        return gson;
    }

    public SharedClient getClient() {
        return client;
    }

    public String sereliaze(AuthBody authBody) {
        return this.gson.toJson(authBody, AuthBody.class);
    }
}
