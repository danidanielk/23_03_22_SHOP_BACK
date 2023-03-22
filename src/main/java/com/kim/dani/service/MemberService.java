package com.kim.dani.service;


import com.kim.dani.dtoGet.MemberLoginGetDto;
import com.kim.dani.dtoGet.MemberSigninGetDto;
import com.kim.dani.dtoSet.AuthSetDto;
import com.kim.dani.dtoSet.MemberLoginSetDto;
import com.kim.dani.dtoSet.TokenSetDto;
import com.kim.dani.entity.Auth;
import com.kim.dani.entity.Member;
import com.kim.dani.entity.QMember;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final QMember member = QMember.member;
    private final JwtTokenV2 jwtTokenV2;


    public boolean signin (MemberSigninGetDto memberSigninGetDto){
        String pw = memberSigninGetDto.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderPassword = passwordEncoder.encode(pw);
        if(memberSigninGetDto.getEmail().equals("master@master")){
        Member member1 = new Member(null, memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(), encoderPassword, Auth.MANAGER);
            try {
            memberRepository.save(member1);
            return true;
            }catch (Exception e){
            return false;
            }
        }
        Member member1 = new Member(null, memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(), encoderPassword, Auth.CUSTOMER);
        try {
            memberRepository.save(member1);
            return true;
        }catch (Exception e){
            return false;
        }
        }

    public MemberLoginSetDto login(MemberLoginGetDto memberLoginGetDto, HttpServletResponse res) {
        Member getMember = queryFactory
                .selectFrom(member)
                .where(member.Email.eq(memberLoginGetDto.getEmail()))
                .fetchOne();
        String plainEmail = memberLoginGetDto.getEmail();
        String plainPassword = memberLoginGetDto.getPassword();
        String hashedPassword = getMember.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean decode = passwordEncoder.matches(plainPassword, hashedPassword);
        if (decode) {
            TokenSetDto tokenSetDto = jwtTokenV2.saveTokenCookie(res,plainEmail);
            MemberLoginSetDto loginDto = new MemberLoginSetDto(plainEmail, getMember.getAuth(),tokenSetDto.getAccessToken(),tokenSetDto.getRefreshToken());
            return loginDto;
        }
        return null;

    }

    public AuthSetDto auth(HttpServletRequest req){
        String email = jwtTokenV2.tokenValidatiorAndGetEmail(req);
        if (email ==null){
            return null;
        }
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.Email.eq(email))
                .fetchOne();

        AuthSetDto authSetDto = new AuthSetDto(member1.getAuth());
        return authSetDto;
    }
}
