package com.example.demo;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import org.json.JSONObject;

@RestController
public class Test {
    private int count = 0;

    @GetMapping("/count")
	public String count() {
        JSONObject message = new JSONObject();
        message.put("count", count++);
        message.put("message", "Witaj na stronie");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            Jwt jwt = (Jwt)authentication.getPrincipal();
            message.put("name", jwt.getClaimAsString("preferred_username"));
            message.put("XX", authorities);
        }
        else {
            message.put("XX", "NO access");
        }

        return message.toString();
    }
    
    @GetMapping("/vip")
	public String vip() {
        JSONObject message = new JSONObject();
        message.put("message", "Witaj vip");

        return message.toString();
	}
}
