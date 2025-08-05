package webserver;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    private static final String TEST_DIR = "./src/test/resources/";

    @Test
    public void request_GET() throws IOException {
        InputStream in = new FileInputStream(TEST_DIR + "Http_GET.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest(br);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("jwkim.oa");
    }

    @Test
    public void request_POST() throws IOException {
        InputStream in = new FileInputStream(TEST_DIR + "Http_POST.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest(br);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("jwkim.oa");
        assertThat(httpRequest.getCookie("logined")).isEqualTo("false");
    }
}