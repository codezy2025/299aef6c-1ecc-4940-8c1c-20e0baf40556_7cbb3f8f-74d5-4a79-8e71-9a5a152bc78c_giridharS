package com.java.coreTemplate.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {
    @Value("${oauth2.provider.token-uri}")
    private String tokenUri;

    @Value("${oauth2.provider.user-info-uri}")
    private String userInfoUri;

    @Value("${oauth2.client.id}")
    private String clientId;

    @Value("${oauth2.client.secret}")
    private String clientSecret;

    @Value("${oauth2.client.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        Map<String, Object> response = restTemplate.postForObject(tokenUri, params, Map.class);
        return (String) response.get("access_token");
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        return restTemplate.getForObject(userInfoUri + "?access_token=" + accessToken, Map.class);
    }
}