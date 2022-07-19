package com.api.events.json_security;


import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationVerificationFilter(AuthenticationManager authManager) {
        super(authManager);
    }



    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String reqHeader = req.getHeader(SecurityConstants.HEADER_STRING);

        if (reqHeader == null || !reqHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            //
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {

        //
        String sequritytoken = req.getHeader(SecurityConstants.HEADER_STRING);
        if (sequritytoken == null) {
            return null;
        }
        String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                .verify(sequritytoken.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getSubject();
        if (!(user == null)) {
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
        return null;
    }

}