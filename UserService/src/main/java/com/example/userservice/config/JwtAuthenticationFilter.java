package com.example.userservice.config;

import com.example.userservice.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (isRegistrationRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromRequest(request);
        if (jwt != null && isTokenValid(jwt)) {
            setAuthenticationContext(jwt, request);
        }

        System.out.println("JwtAuthentication passed");
        System.out.println("jwt: " + jwt    );
        filterChain.doFilter(request, response);
        System.out.println("JwtAuthentication passed");
    }

    private boolean isRegistrationRequest(HttpServletRequest request) {
        return request.getServletPath().contains("/users/register") || request.getServletPath().contains("/users/login");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String jwt) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            return tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked() && jwtService.isTokenValid(jwt, userDetails))
                    .orElse(false);
        }
        return false;
    }

    private void setAuthenticationContext(String jwt, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(jwt));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}

