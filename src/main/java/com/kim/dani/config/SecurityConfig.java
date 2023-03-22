package com.kim.dani.config;


import com.kim.dani.jwt.JwtAuthenticationFilter;
import com.kim.dani.jwt.JwtTokenV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    private JwtTokenV2 jwtToken;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth").authenticated() // 이경로는 JWT 인증된 사용자만 접근가능
//                .antMatchers("/signin").permitAll() // /a 경로는 인증없이 접근가능
                .antMatchers().permitAll() // authenticated() 로 설정한 경로 외 모두 접근가능
                .antMatchers("/master").hasRole("USER") // 이경로는 JWT + AuthenticationManagerBuilder 로 설정한 권한도있어야한다.
                .and()
//                .formLogin()
//                .loginPage("/member/login") //로그인 페이지 경로
//                .permitAll()
//                .and()
                .logout()
                .permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtToken), UsernamePasswordAuthenticationFilter.class);
                //JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가.
    }

    //Member enum 으로 구분된 manager 를 설정.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER");
    }

}
