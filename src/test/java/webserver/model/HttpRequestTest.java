package webserver.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getParameter("userId")).isEqualTo("jwkim.oa");
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getParameter("userId")).isEqualTo("jwkim.oa");
    }
}