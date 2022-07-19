package com.api.events.json_security;

import com.api.events.entities.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {


    @Autowired
    RestTemplate restTemplate;

    private AuthenticationManager authenticationManager;

    public JWTAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(),
                    new ArrayList<>());
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            System.out.println(authenticate+" ************************");
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ///afte this go to successfulAuthentication
    }

  //  static String jwttoken;
    static public HashMap<String,String> map= new HashMap<>();
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create().withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);



        /**
        map.put("token",token);
        boolean response = false;

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", "Bearer " +token);
        HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
         */

    }
/**
    public static String passToken() {

        return jwttoken;

        String response = null;

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", "Bearer " +token);
        HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> helloResponse = restTemplate.exchange("http://localhost:8082/home", HttpMethod.GET, jwtEntity,
                String.class);

        if (helloResponse.getStatusCode().equals(HttpStatus.OK)) {
            response = helloResponse.getBody();
        }
        return "success";
    }**/

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}