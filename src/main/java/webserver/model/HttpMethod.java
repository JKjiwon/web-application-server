package webserver.model;

public enum HttpMethod {
    GET, POST, OPTION;

    public boolean isPost() {
        return this.equals(POST);
    }

    public boolean isGet() {
        return this.equals(GET);
    }
}
