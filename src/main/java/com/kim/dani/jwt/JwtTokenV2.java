package com.kim.dani.jwt;


import com.kim.dani.dtoSet.TokenSetDto;
import com.kim.dani.entity.Auth;
import com.kim.dani.entity.Member;
import com.kim.dani.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenV2 {

    private final JPAQueryFactory queryFactory;
    private final QMember qmember = QMember.member;

    private String accessTK = "accessTK";

    private String refreshTK = "refreshTK";


    @Value("${secretKey}")
    private String secretKey;

    private String createToken(String email, boolean isRefreshToken){
        Claims claims = Jwts.claims();
        claims.put("email", email);
        String tokenName = isRefreshToken ? refreshTK : accessTK;
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

        Cookie accessCookie = new Cookie(accessTK, accessToken);
        accessCookie.setMaxAge(60*10); //10분
//        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");

        Cookie refreshCookie = new Cookie(refreshTK, refreshToken);
        refreshCookie.setMaxAge(1*24*60*60); //1일
//        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        res.addCookie(accessCookie);
        res.addCookie(refreshCookie);
        TokenSetDto tokenSetDto = new TokenSetDto(accessToken, refreshToken);
        return tokenSetDto;
    }



    public String getToken(HttpServletRequest req){
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }



    public Authentication getAuthentication(String token) {
        UserDetails userDetails = User.builder()
                .username(getUsername(token))
                .password("")
                .roles(getRoles(token).toArray(new String[0]))
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }



    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().get("email",String.class);
    }



    @SuppressWarnings("unchecked")
    private List<String> getRoles(String token) {

        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        String email = claims.get("email", String.class);
        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(email))
                .fetchOne();
        // MASTER,CUSTOMER 권한 부여
        if (member.getAuth().equals(Auth.MANAGER)) {
            System.out.println("나는 마스터입니당--------------------------");
            return Collections.singletonList("MASTER");
        } else if (member.getAuth().equals(Auth.CUSTOMER)) {
            System.out.println("나는 회원입니당----------------------------");
            return Collections.singletonList("CUSTOMER");
        }
        return Collections.emptyList();
    }



    public String tokenValidatiorAndGetEmail(HttpServletRequest req,HttpServletResponse res ) {
        String token = getToken(req);
        if (token != null) {
            try {
                String email = getUsername(token);
                return email;
            } catch (ExpiredJwtException e) {
//                 Access 토큰이 만료된 경우
//                newAccessToken(req, res);
            }
        }
        return null;
    }



    public String newAccessToken(HttpServletRequest req,HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        String refreshToken =null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(refreshTK)) {
                refreshToken = cookie.getValue();
                break;
            }
        }
            if (refreshToken!=null) {
                try {
                    String email = getUsername(refreshToken);
                        // 새로운 access 토큰 발급
                        String newAccessToken = createToken(email, false);
                        Cookie newAccessCookie = new Cookie(accessTK, newAccessToken);
                        newAccessCookie.setMaxAge(60 * 10); // 10분
//                        newAccessCookie.setHttpOnly(true);
                        newAccessCookie.setPath("/");
                        res.addCookie(newAccessCookie);

                        return newAccessToken;

                } catch (ExpiredJwtException ex) {
                    // Refresh 토큰도 만료된 경우
                    return null;
                }
            }
            return null;
    }
}
