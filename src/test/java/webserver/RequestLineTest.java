package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.core.HttpMethod;
import webserver.core.RequestLine;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestLineTest {

    @Test
    @DisplayName("생성자로 들어오는 값이 Null이면 IOException 발생")
    public void nullLine() {
        assertThatThrownBy(() -> new RequestLine(null))
                .isInstanceOf(IOException.class)
                .hasMessage("EOF: RequestLine isn't received");
    }

    @Test
    @DisplayName("생성자로 잘못된 형식의 값이 들어오면 IOException 발생")
    public void invalidRequestLine() {
        assertThatThrownBy(() -> new RequestLine("GET"))
                .isInstanceOf(IOException.class)
                .hasMessage("Invalid RequestLine");
    }

    @Test
    @DisplayName("RequestLine 생성")
    public void create() throws IOException {
        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("QueryParam 파싱")
    public void parseQueryParam() throws IOException {
        RequestLine requestLine = new RequestLine("GET /user/create?userId=jwkim.oa&password=1234 HTTP/1.1");
        Map<String, String> queryParams = requestLine.getQueryParams();
        assertThat(queryParams.get("userId")).isEqualTo("jwkim.oa");
        assertThat(queryParams.get("password")).isEqualTo("1234");
    }
}