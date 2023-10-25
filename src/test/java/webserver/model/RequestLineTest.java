package webserver.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestLineTest {

    @Test
    public void create_method() {
        RequestLine line = new RequestLine("GET /users?userId=jwkim&name=jiwon HTTP/1.1");

        assertThat(line.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(line.getPath()).isEqualTo("/users");

        assertThat(line.getParameters()).hasSize(2);
        assertThat(line.getParameters().get("userId")).isEqualTo("jwkim");
        assertThat(line.getParameters().get("name")).isEqualTo("jiwon");
    }

    @Test
    public void illegal_request_line() {
        assertThatThrownBy(()-> new RequestLine("GET HTTP/1.1"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}