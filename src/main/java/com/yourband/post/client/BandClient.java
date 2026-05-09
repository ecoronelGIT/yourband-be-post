package com.yourband.post.client;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BandClient {

    private static final Logger log = LoggerFactory.getLogger(BandClient.class);

    private final RestTemplate restTemplate;

    @Value("${services.band.url}")
    private String bandServiceUrl;

    /**
     * Obtiene los IDs de bandas que sigue el usuario.
     * Llama a GET /v1/bands/following con el header X-User-Id.
     */
    public List<UUID> getFollowedBandIds(UUID userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());

            var response = restTemplate.exchange(
                    bandServiceUrl + "/v1/bands/following",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<UUID>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error llamando a be-band para obtener bandas seguidas: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
