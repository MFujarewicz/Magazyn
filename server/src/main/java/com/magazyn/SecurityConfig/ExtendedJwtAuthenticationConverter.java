package com.magazyn.SecurityConfig;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtendedJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final String client_id;
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @SuppressWarnings("unchecked")
    private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt, final String client_id) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(client_id);
        if (resource == null) {
            return Collections.emptySet();
        }

        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        if (resourceRoles == null) {
            return Collections.emptySet();
        }

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role_name : resourceRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role_name));
        }

        return authorities;
    }

    public ExtendedJwtAuthenticationConverter(String client_id)
    {
        this.client_id = client_id;
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt source)
    {
        Collection<GrantedAuthority> authorities = 
            Stream.concat(defaultGrantedAuthoritiesConverter.convert(source).stream(), extractResourceRoles(source, client_id).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(source, authorities);
    }
}