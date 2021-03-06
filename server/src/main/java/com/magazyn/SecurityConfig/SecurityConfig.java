package com.magazyn.SecurityConfig;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Value("${spring.security.oauth2.client.provider.keycloak.jwk-set-uri}")
    private String jwkSetUri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests(authorizeRequests -> authorizeRequests
//        .antMatchers("/**").permitAll() // wrazie testow odkomentowac
        .antMatchers("/**/admin/**").hasRole("administrator")
        .antMatchers("/**/api/job/me/").hasAnyRole("user", "administrator")
        .antMatchers("/map.png").permitAll()
        .antMatchers("/home.svg").permitAll()
        .antMatchers("/**/api/job/gen").hasAnyRole("user", "administrator")
        .antMatchers("/**/api/job/confirm").hasAnyRole("user", "administrator")
        .antMatchers("/**").hasAnyRole("manager", "administrator")
        .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new ExtendedJwtAuthenticationConverter("Warehouse"));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        var cors_configuration = new CorsConfiguration().applyPermitDefaultValues();
        cors_configuration.setAllowedMethods(Arrays.asList(
            new String[] { "GET", "PUT", "POST", "DELETE"}
        ));

        source.registerCorsConfiguration("/**", cors_configuration);
        return source;
    }
}