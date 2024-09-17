package fr.traqueur.shared.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.shared.api.domain.Schema;
import fr.traqueur.shared.api.domain.Table;
import fr.traqueur.shared.api.requests.EndPoints;
import fr.traqueur.shared.api.updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SharedAPI {

    public static final SharedURL API_URL = new SharedURL("http://localhost:8000/api");

    private final Schema schema;
    private final Gson gson;
    private final Logger logger;
    private final SharedClient client;

    private final boolean debug;

    public SharedAPI(@Nullable JavaPlugin instance, UUID plugin, UUID server) {
        this.logger = instance == null ? Logger.getLogger("SharedAPI") : instance.getLogger();
        this.gson = this.buildGson();
        this.schema = new Schema(new ConcurrentHashMap<>());
        this.debug = true;
        try {
            this.client = new SharedClient(this, this.gson, plugin, server);
        } catch (ExecutionException | InterruptedException e) {
            this.logger.severe("An error occurred while trying to connect to the API.");
            throw new RuntimeException(e);
        }
        if(this.isDebug()) {
            this.logger.info("The SharedAPI has been initialized.");
            Updater.checkUpdates();
        }
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    public <T extends Table> void registerTable(String name, Class<T> table) {
        if (!table.isRecord()) {
            throw new IllegalArgumentException("The class " + table.getName() + " is not a record class.");
        }
        this.schema.addTable(name, Table.getColumns(table));
        if(this.isDebug()) {
            this.logger.info("The table " + table.getSimpleName() + " has been registered.");
        }
    }

    public void loadSchema() {
        this.client.sendSchema(this.schema);
    }

    public Logger getLogger() {
        return logger;
    }

    public Gson getGson() {
        return gson;
    }

    public boolean isDebug() {
        return debug;
    }

    public SharedClient getClient() {
        return client;
    }
}
