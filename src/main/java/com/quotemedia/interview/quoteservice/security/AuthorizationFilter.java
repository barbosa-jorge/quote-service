package com.quotemedia.interview.quoteservice.security;

import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
        super(authManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

        String user = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (StringUtils.isEmpty(user)) {
            return null;
        }

        Optional<UserEntity> userEntity = userRepository.findByEmail(user);

        if (!userEntity.isPresent()) {
            return null;
        }

        UserPrincipal userPrincipal = new UserPrincipal(userEntity.get());
        return new UsernamePasswordAuthenticationToken(userPrincipal, null,
                userPrincipal.getAuthorities());

    }
}