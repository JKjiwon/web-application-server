package webserver.model;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestTest {

    private static final String FILE_DIR_PATH = "./src/test/resources/";

//    GET /user/create?name=jiwon&password=1234&email=jwkim.oa@gmail.com HTTP/1.1
//    Host: localhost:8080
//    Connection: keep-alive
//    Accept: */*
//
    @Test
    public void HTTP_GET() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_DIR_PATH + "HTTP_GET.txt");
        HttpRequest httpRequest = new HttpRequest(fileInputStream);

        // request line
        HttpRequest.RequestLine requestLine = httpRequest.getRequestLine();
        assertThat(requestLine.getHttpMethod()).isEqualTo("GET");
        assertThat(requestLine.getUri()).isEqualTo("/user/create?name=jiwon&password=1234&email=jwkim.oa@gmail.com");
        assertThat(requestLine.getHttpVersion()).isEqualTo("HTTP/1.1");

        // headers
        Map<String, String> headers = httpRequest.getHeaders();
        assertThat(headers.get("Host")).isEqualTo("localhost:8080");
        assertThat(headers.get("Connection")).isEqualTo("keep-alive");
        assertThat(headers.get("Accept")).isEqualTo("*/*");

        // parameters
        Map<String, String> parameters = httpRequest.getParameters();
        assertThat(parameters.get("name")).isEqualTo("jiwon");
        assertThat(parameters.get("password")).isEqualTo("1234");
        assertThat(parameters.get("email")).isEqualTo("jwkim.oa@gmail.com");
    }


//    POST /user/create HTTP/1.1
//    Host: localhost:8080
//    Connection: keep-alive
//    Accept: */*
//    Content-Length: 49
//
//    name=jiwon&password=1234&email=jwkim.oa@gmail.com
    @Test
    public void HTTP_POST() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_DIR_PATH + "HTTP_POST.txt");
        HttpRequest httpRequest = new HttpRequest(fileInputStream);

        // request line
        HttpRequest.RequestLine requestLine = httpRequest.getRequestLine();
        assertThat(requestLine.getHttpMethod()).isEqualTo("POST");
        assertThat(requestLine.getUri()).isEqualTo("/user/create");
        assertThat(requestLine.getHttpVersion()).isEqualTo("HTTP/1.1");

        // headers
        Map<String, String> headers = httpRequest.getHeaders();
        assertThat(headers.get("Host")).isEqualTo("localhost:8080");
        assertThat(headers.get("Connection")).isEqualTo("keep-alive");
        assertThat(headers.get("Accept")).isEqualTo("*/*");
        assertThat(headers.get("Content-Length")).isEqualTo("49");

        // parameters
        Map<String, String> parameters = httpRequest.getParameters();
        assertThat(parameters.get("name")).isEqualTo("jiwon");
        assertThat(parameters.get("password")).isEqualTo("1234");
        assertThat(parameters.get("email")).isEqualTo("jwkim.oa@gmail.com");
    }
}