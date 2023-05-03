package webserver.model;

public enum ContentType {
    HTML("text/html;charset=utf-8"), CSS("text/css");
    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
