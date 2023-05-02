package webserver.model;

enum HttpStatus {
    OK(200, "OK"),
    REDIRECT(302, "Redirect");

    private int code;
    private String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
