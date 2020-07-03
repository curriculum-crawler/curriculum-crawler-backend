package edu.harvard.cscis71.curriculum.api;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

public class HomeControllerWebClient {
    private static final DefaultUriBuilderFactory uriBuilder = new DefaultUriBuilderFactory();
    private final String endpoint;
    private final WebClient webClient;

    public HomeControllerWebClient(String scheme, String host, int port) {
        this.endpoint = uriBuilder.builder().scheme(scheme).host(host).port(port).build().toString();
        this.webClient = WebClient.builder().baseUrl(endpoint).build();
    }

    public String getResult() {
        Mono<ClientResponse> result = webClient.get().uri("/").accept(MediaType.APPLICATION_JSON).exchange();
        return result.flatMap(res -> res.bodyToMono(String.class)).block();
    }

}
