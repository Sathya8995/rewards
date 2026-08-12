package com.loyalty.rewards.reward.filter;

import com.loyalty.rewards.reward.security.CustomUserDetailsService;
import com.loyalty.rewards.reward.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = authHeader.substring(7);

            String username = jwtService.extractUsername(jwtToken);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 1. Load the user from DB
                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                // 2. Validate JWT against that user
                if (jwtService.isTokenValid(jwtToken, userDetails)) {

                    // 3. Create authenticated Authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Store authenticated user in SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }

            }
        }
        catch (JwtException | IllegalArgumentException ex) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write("""
            {
              "status": 401,
              "message": "Invalid or expired JWT"
            }
            """);

            return;
        }

        filterChain.doFilter(request, response);

    }
}
