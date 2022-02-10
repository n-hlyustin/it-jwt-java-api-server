package com.itransition.simpleapiserver.security;
import com.itransition.simpleapiserver.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";

    private final JwtHelper jwtHelper;

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException, NoSuchElementException {
        getTokenFromRequest((HttpServletRequest) servletRequest)
            .filter(jwtHelper::isTokenValid)
            .map(jwtHelper::getIdFromToken)
            .map(userService::getUserById)
            .map(user -> {
                List<GrantedAuthority> authorities = buildUserAuthority(user.getRole().name());
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            })
            .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String prefix = "Bearer ";
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(prefix)) {
            return Optional.of(bearer.substring(prefix.length()));
        }
        return Optional.empty();
    }

    private List<GrantedAuthority> buildUserAuthority(String userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<>();
        setAuths.add(new SimpleGrantedAuthority(userRoles));
        return new ArrayList<>(setAuths);
    }
}
