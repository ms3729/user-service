package com.rata.userService.config.authentication;

import com.rata.userService.config.aspect.LogRecord;
import com.rata.userService.models.Token;
import com.rata.userService.records.TokenRecord;
import com.rata.userService.services.impl.command.TokenCommandServiceImpl;
import com.rata.userService.services.interfaces.query.UserSessionHistoryQueryService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    public static final String SECRET = "5367566B59bB03373367639792F423F4528482B4D6251655468576D5A71347437";
    private final TokenCommandServiceImpl tokenCommandService;
    private final UserInfoService userInfoService;
    private final UserSessionHistoryQueryService userSessionHistoryQueryService;

    public TokenRecord generateToken(List<String> roles, String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        FlmUserDetails flmUserDetails = userInfoService.loadUserByUsername(userName);
        if (flmUserDetails != null) {
            claims.put("organizationId", flmUserDetails.getOrganizationId());
        }
        return createToken(claims, userName);
    }

    public TokenRecord createToken(Map<String, Object> claims, String userName) {
        Date expirationDate = new Date(System.currentTimeMillis() + (1000 * 3600 * 12));
        String tokenString = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        Token token = tokenCommandService.findById(userName);
        if (token == null) {
            token = new Token();
        }
        token.setId(userName);
        token.setExpiration(extractExpiration(tokenString));
        token.setAccess(tokenString);
        token.setRefresh(UUID.randomUUID().toString());
        tokenCommandService.save(token);
        userSessionHistoryQueryService.save(userName, extractIssueDate(tokenString));
        return new TokenRecord(token.getAccess(), token.getRefresh());
    }

    public TokenRecord doGenerateRefreshToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String lastToken = bearerToken.substring(7);
            String username = extractUsername(lastToken);
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            Map<String, Object> claims = new HashMap<>();
            List<String> roles = userDetails.getAuthorities().stream().filter(Objects::nonNull)
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            claims.put("roles", roles);
            return createToken(claims, userDetails.getUsername());
        }
        return null;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssueDate(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateUser(String token) {
        final String username = extractUsername(token);
        Token cachedToken = tokenCommandService.findById(username);
        return cachedToken != null &&
                Objects.equals(cachedToken.getAccess(), token) &&
                extractExpiration(token).getTime() == cachedToken.getExpiration().getTime() &&
                !isTokenExpired(token);
    }

    public void validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(authToken);
        } catch (Exception ex) {
            log.error("{}", new LogRecord("local", "user-service", this.getClass().getName(), "validateToken", ""));
        }
    }

    public String validateExternalToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(authToken);
            String username = extractUsername(authToken);
            Token token = tokenCommandService.findById(username);
            if (token.getAccess().equals(authToken)) {
                return username;
            } else {
                return "null";
            }
        } catch (Exception ex) {
            log.error("{}", new LogRecord("local", "user-service", this.getClass().getName(), "validateToken", ""));
        }
        return "null";
    }

} 