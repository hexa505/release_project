package com.project.release.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private Long id;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           Long id) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.id = id;
    }

    public static OAuthAttributes of(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        System.out.println("OAuthAttributes: " + attributes);

        return OAuthAttributes.builder()
                .id(Long.parseLong((String) response.get("id")))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
