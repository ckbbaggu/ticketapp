package com.bagaria.ticketapp8.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();


        jwt.getClaims().forEach((k, v) -> System.out.println(k + " = " + v));

        // Read roles
        List<String> roles = jwt.getClaimAsStringList("https://ticket-api/roles");

        System.out.println("Roles: " + roles);

        if (roles != null) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }
        System.out.println("Authorities: " + authorities);

        // Read permissions
        List<String> permissions = jwt.getClaimAsStringList("permissions");

        if (permissions != null) {
            permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        }

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("https://ticket-api/email"));
    }
}