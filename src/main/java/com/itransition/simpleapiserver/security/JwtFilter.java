package com.itransition.simpleapiserver.security;
import com.itransition.simpleapiserver.entities.User;
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
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";

    private final JwtHelper jwtHelper;

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, NoSuchElementException {
        Optional<String> token = getTokenFromRequest((HttpServletRequest) servletRequest);
        Optional.of(token)
            .get()
            .filter(data -> jwtHelper.validateToken(data))
            .ifPresent(validToken -> {
                Long id = jwtHelper.getIdFromToken(validToken);
                User user = userService.getUserById(id);
                List<GrantedAuthority> authorities = buildUserAuthority(user.getRole());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return Optional.of(bearer.substring(7));
        }
        return Optional.empty();
    }

    private List<GrantedAuthority> buildUserAuthority(String userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<>();

        setAuths.add(new SimpleGrantedAuthority(userRoles));

        List<GrantedAuthority> Result = new ArrayList<>(setAuths);

        return Result;
    }
}
