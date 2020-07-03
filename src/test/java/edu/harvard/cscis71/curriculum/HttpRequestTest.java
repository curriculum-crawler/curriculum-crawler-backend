package edu.harvard.cscis71.curriculum;

import com.google.common.truth.Truth;
import edu.harvard.cscis71.curriculum.api.HomeControllerWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @Autowired
    private HomeController controller;

    @LocalServerPort
    private int port;
    private URI endpoint;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        Truth.assertThat(controller).isNotNull();
        Truth.assertThat(restTemplate).isNotNull();

        UriBuilderFactory f = new DefaultUriBuilderFactory();
        endpoint = f.builder().scheme("http").host("localhost").port(port).path("/").build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void rootRequestShouldReturnRedirect() {
        Truth.assertThat(restTemplate.getForObject(endpoint, String.class)).contains("Swagger UI");
    }

    @Test
    public void homeRequestWebClientShouldReturnRedirectResponse() {
        HomeControllerWebClient client = new HomeControllerWebClient("http", "localhost", port);
        Truth.assertThat(client.getResult()).isNull();
    }
}
