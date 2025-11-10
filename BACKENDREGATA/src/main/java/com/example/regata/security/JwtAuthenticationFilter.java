package com.example.regata.security;

import com.example.regata.service.UsuarioService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT siguiendo el patrón del ejemplo del profesor
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
            if (invalidAuthHeader(authHeader)) {
                // Si no hay header o es inválido, continuar
                filterChain.doFilter(request, response);
                return;
            }
            
            String jwt = authHeader.substring(7);
            
            if (jwtService.isTokenExpired(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            String username = jwtService.extractUsername(jwt);
            
            if (StringUtils.isEmpty(username)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // Si el usuario no existe en la BD, lanzará UsernameNotFoundException
            UserDetails userDetails = usuarioService.userDetailsService()
                    .loadUserByUsername(username);
            
            // Si la información de autenticación no ha sido extraída previamente
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
            
            filterChain.doFilter(request, response);
        } catch (SignatureException e) {
            log.error("Invalid token:", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }
    }
    
    private boolean invalidAuthHeader(final String authHeader) {
        return StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ");
    }
}
