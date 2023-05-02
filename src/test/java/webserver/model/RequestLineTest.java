package webserver.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLineTest {

    @Test
    public void create_method() {
        RequestLine requestLine1 = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(requestLine1.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine1.getPath()).isEqualTo("/index.html");

        RequestLine requestLine2 = new RequestLine("POST /index.html HTTP/1.1");
        assertThat(requestLine2.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(requestLine2.getPath()).isEqualTo("/index.html");
    }

    @Test
    public void create_path_and_params() {
        RequestLine requestLine = new RequestLine("GET /user/create?userId=jiwon&password=pass HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/user/create");
        assertThat(requestLine.getParams().get("userId")).isEqualTo("jiwon");
        assertThat(requestLine.getParams().get("password")).isEqualTo("pass");
    }

}