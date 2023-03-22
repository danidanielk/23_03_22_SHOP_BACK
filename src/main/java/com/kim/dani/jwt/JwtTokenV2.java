package com.kim.dani.jwt;


import com.kim.dani.dtoSet.TokenSetDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class JwtTokenV2 {

    private String accessTK = "accessTK";
    private String refreshTK = "refreshTK";


    @Value("${secretKey}")
    private String secretKey;

    private String createToken(String email, boolean isRefreshToken){
        Claims claims = Jwts.claims();
        claims.put("email", email);
        String tokenName = isRefreshToken ? "refreshTK" : "accessTK";
        long expireationTime  = isRefreshToken ? 1*24*60*60*1000L : 60*10*1000L; //1일 or 10분
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public TokenSetDto saveTokenCookie(HttpServletResponse res, String email){
        String accessToken = createToken(email, false);
        String refreshToken = createToken(email, true);

        Cookie accessCookie = new Cookie("accessTK", accessToken);
        accessCookie.setMaxAge(60*10); //10분
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");

        Cookie refreshCookie = new Cookie("refreshTK", refreshToken);
        refreshCookie.setMaxAge(1*24*60*60); //1일
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        res.addCookie(accessCookie);
        res.addCookie(refreshCookie);
        TokenSetDto tokenSetDto = new TokenSetDto(accessToken, refreshToken);
        return tokenSetDto;
    }


    public boolean tokenValidator(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(accessTK)) {
                    String token = cookie.getValue();
                    try {
                        Jwts.parser()
                                .setSigningKey(secretKey.getBytes())
                                .parseClaimsJws(token);
                        return true;
                    } catch (JwtException e) {
                        return false;
                    }
                } else if (cookie.getName().equals(refreshTK)) {
                    String token = cookie.getValue();
                    try {
                        Jwts.parser()
                                .setSigningKey(secretKey.getBytes())
                                .parseClaimsJws(token);
                        return true;
                    } catch (JwtException e) {
                        return false;
                    }

                }


            }
        }
        return false;
    }



    public String tokenValidatiorAndGetEmail(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(accessTK)){
                String token = cookie.getValue();
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes())
                        .parseClaimsJws(token)
                        .getBody();
                if(claims != null){
                    String email = (String) claims.get("email");
                    return email;
                }
            } else if (cookie.getName().equals(refreshTK)) {
                String token = cookie.getValue();
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes())
                        .parseClaimsJws(token)
                        .getBody();
                if(claims != null){
                    String email = (String) claims.get("email");
                    return email;
                }
            }
        }
        return null;
    }


}
