package com.magazyn.SecurityConfig;

import java.util.Collection;

import org.json.JSONObject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO REMOVE THIS CLASS!!!!!!!!!
//ONLY FOR TESTING
@SuppressWarnings("unchecked")
@RestController
public class SecurityInfo {
    
    @GetMapping("sec_info/**")
    String getSecInfo() {
        JSONObject message = new JSONObject();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            Jwt jwt = (Jwt)authentication.getPrincipal();
            message.put("name", jwt.getClaimAsString("preferred_username"));
            message.put("XX", authorities);
            message.put("ID", jwt.getSubject());
        }
        else {
            message.put("XX", "NO access");
        }

        return message.toString();
    }

    @GetMapping("sec_test/**")
    String test1() {
        JSONObject message = new JSONObject();

        message.put("XX", "OK");

        return message.toString();
    }

    @GetMapping("sec_test/abc")
    String test2() {
        JSONObject message = new JSONObject();

        message.put("XX", "OK");

        return message.toString();
    }

    @Secured("ROLE_manager")
    @GetMapping("sec_test/aaa")
    String test3() {
        JSONObject message = new JSONObject();

        message.put("XX", "SUPER OK");

        return message.toString();
    }
}
