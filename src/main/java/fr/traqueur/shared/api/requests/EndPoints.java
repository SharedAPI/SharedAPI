package fr.traqueur.shared.api.requests;

public enum EndPoints {

    AUTH("/auth/"),
    SCHEMA("/schemas/{plugin_uuid}/"),
    ;

    private String path;
    private boolean isGet;

    EndPoints(String path) {
        this.path = path;
    }

    public EndPoints match(String key, String value) {
        if(!this.path.contains("{" + key + "}")) {
            throw new IllegalArgumentException("The key " + key + " is not present in the path " + this.path);
        }
        this.path = this.path.replace("{" + key + "}", value);
        return this;
    }

    public EndPoints get(String key, String value) {
        if(!isGet) {
            isGet = true;
            this.path = this.path + "?";
        } else {
            this.path = this.path + "&";
        }
        this.path = this.path + key + "=" + value;
        return this;
    }

    public String getPath() {
        return path;
    }

}
