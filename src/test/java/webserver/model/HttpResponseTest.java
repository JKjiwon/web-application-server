package webserver.model;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForwardHTML() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward_HTML.txt"));
        response.forward("/index.html");
    }

    @Test
    public void responseForwardCSS() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward_CSS.txt"));
        response.forward("/css/styles.css");
    }

    @Test
    public void responseRedirect() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        response.senRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.senRedirect("/index.html");
    }

    private OutputStream createOutputStream(String fileName) throws IOException {
        return Files.newOutputStream(Paths.get(testDirectory + fileName));
    }

}