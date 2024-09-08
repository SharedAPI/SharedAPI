package fr.traqueur.shared.api;

import fr.traqueur.shared.api.updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SharedAPI {

    public static final SharedURL API_URL = new SharedURL("http://localhost:8000/api");
    private final Logger logger;

    private final SharedClient client;

    public SharedAPI(JavaPlugin instance, UUID plugin, UUID server) {
        this.logger = instance.getLogger();
        try {
            this.client = new SharedClient(plugin, server);
        } catch (ExecutionException | InterruptedException e) {
            this.logger.severe("An error occurred while trying to connect to the API.");
            throw new RuntimeException(e);
        }
        Updater.checkUpdates();
    }

    public SharedClient getClient() {
        return client;
    }
}
