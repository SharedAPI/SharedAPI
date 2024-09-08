package fr.traqueur.shared.api;

import fr.traqueur.shared.api.requests.EndPoints;

import java.net.URI;

public class SharedURL {

    private String path;

    public SharedURL(String root) {
        this.path = root;
    }

    public URI resolve(EndPoints path) {
        return this.resolve(path.getPath());
    }

    public URI resolve(String path) {
        this.path = this.path + path;
        return URI.create(this.path);
    }

}
