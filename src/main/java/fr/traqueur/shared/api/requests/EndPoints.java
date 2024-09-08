package fr.traqueur.shared.api.requests;

public enum EndPoints {

    AUTH("/auth"),
    ;

    private String path;
    private boolean isGet;

    EndPoints(String path) {
        this.path = path;
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
