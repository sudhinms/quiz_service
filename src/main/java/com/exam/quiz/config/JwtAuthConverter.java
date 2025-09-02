package com.exam.quiz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${quiz_app.jwt.auth.converter.principal-attribute}")
    private String PRINCIPLE_ATTRIBUTE;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> jwtAuthorities =
                Optional.ofNullable(grantedAuthoritiesConverter.convert(jwt))
                        .orElse(Collections.emptyList());

        Collection<? extends GrantedAuthority> resourceRoles =
                Optional.ofNullable(extractResourceRoles(jwt))
                        .orElse(Collections.emptyList());

        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtAuthorities.stream(),
                resourceRoles.stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalName(jwt));
    }


    private String getPrincipalName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (PRINCIPLE_ATTRIBUTE != null) {
            claimName = PRINCIPLE_ATTRIBUTE;
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");

        if (resourceAccess == null || !resourceAccess.containsKey("roles")) {
            return Collections.emptySet();
        }
        Object rolesMap = resourceAccess.get("roles");

        if (!(rolesMap instanceof Collection<?> roles)) {
            return Collections.emptySet();
        }
        return roles.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

}
