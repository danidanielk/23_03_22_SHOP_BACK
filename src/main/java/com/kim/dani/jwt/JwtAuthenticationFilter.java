package com.kim.dani.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//JwtToken 클래스를 이용하여 JWT토큰 검증을 처리하고 토큰이 유효한경우 인증 정보를 설정.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenV2 jwtToken;

    public  JwtAuthenticationFilter(JwtTokenV2 jwtToken){
        this.jwtToken = jwtToken;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(jwtToken.tokenValidator(request)){
            //JWT 토큰이 융효한 경우 , 인증 정보 설정.
            String email = jwtToken.tokenValidatiorAndGetEmail(request,response);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
}
