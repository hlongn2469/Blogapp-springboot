package com.springboot.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider token_provider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get JWT token from http request
        String token = getJWTfromToken(request);

        // validate token
        if(StringUtils.hasText(token) && token_provider.validateToken(token)){
            // get username from token
            String username = token_provider.getUserNameFromJWT(token);

            // load user associated with token
            UserDetails details = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth_token = new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities()
            );
            auth_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // set spring security
            SecurityContextHolder.getContext().setAuthentication(auth_token);
            filterChain.doFilter(request,response);
        }
    }

    private String getJWTfromToken(HttpServletRequest request){
        String bearer_token = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer_token) && bearer_token.startsWith("Bearer ")){
            return bearer_token.substring(7, bearer_token.length());
        }
        return null;
    }
}
