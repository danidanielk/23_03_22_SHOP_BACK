package com.kim.dani.service;


import com.kim.dani.dtoGet.MemberLoginGetDto;
import com.kim.dani.dtoGet.MemberSigninGetDto;
import com.kim.dani.dtoSet.AuthSetDto;
import com.kim.dani.dtoSet.MemberLoginSetDto;
import com.kim.dani.dtoSet.MyPageSetDto;
import com.kim.dani.dtoSet.TokenSetDto;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final QMember qmember = QMember.member;
    private final QCart qCart = QCart.cart;
    private final QCartAndProduct qCartAndProduct = QCartAndProduct.cartAndProduct;
    private final QProduct qProduct = QProduct.product;
    private final JwtTokenV2 jwtTokenV2;


    //회원가입
    public boolean signin (MemberSigninGetDto memberSigninGetDto){
        String pw = memberSigninGetDto.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderPassword = passwordEncoder.encode(pw);
        if(memberSigninGetDto.getEmail().equals("master@master")){
        Member member1 = new Member(null, memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(),
                encoderPassword, Auth.MANAGER,null,null);
            try {
            memberRepository.save(member1);
            return true;
            }catch (Exception e){
            return false;
            }
        }
        Member member1 = new Member(null, memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(),
                encoderPassword, Auth.CUSTOMER,null,null);
        try {
            memberRepository.save(member1);
            return true;
        }catch (Exception e){
            return false;
        }
        }


    // 로그인 쿠키에 토큰 보내줌
    public MemberLoginSetDto login(MemberLoginGetDto memberLoginGetDto, HttpServletResponse res) {
        Member getMember = queryFactory
                .selectFrom(qmember)
                .where(qmember.Email.eq(memberLoginGetDto.getEmail()))
                .fetchOne();
        String plainEmail = memberLoginGetDto.getEmail();
        String plainPassword = memberLoginGetDto.getPassword();
        String hashedPassword = getMember.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean decode = passwordEncoder.matches(plainPassword, hashedPassword);
        if (decode) {
            TokenSetDto tokenSetDto = jwtTokenV2.saveTokenCookie(res,plainEmail);
            MemberLoginSetDto loginDto = new MemberLoginSetDto(plainEmail, getMember.getAuth(),
                    tokenSetDto.getAccessToken(),tokenSetDto.getRefreshToken());
            return loginDto;
        }
        return null;

    }

    // 권한 체크후 Mypage에 MANAGER Page || CUSTOMER Page 생성
    public AuthSetDto auth(HttpServletRequest req){
        String email = jwtTokenV2.tokenValidatiorAndGetEmail(req);
        if (email ==null){
            return null;
        }
        Member member1 = queryFactory
                .selectFrom(qmember)
                .where(qmember.Email.eq(email))
                .fetchOne();

        AuthSetDto authSetDto = new AuthSetDto(member1.getId(), member1.getAuth());
        return authSetDto;
    }


    //Mypage 리스트 Manager은 전체상품 꺼내주고 Customer은 카트조회해서 꺼내주기
    public List<MyPageSetDto> myPage(Long memberId,HttpServletRequest req) {

        Member member1 = queryFactory
                .selectFrom(qmember)
                .where(qmember.id.eq(memberId))
                .fetchOne();

            List<MyPageSetDto> myPageSetDtos = new ArrayList<>();
        if (member1.getAuth().equals(Auth.CUSTOMER)) {
            List<Product> products = queryFactory
                    .select(qProduct)
                    .from(qmember)
                    .leftJoin(qmember.cart, qCart)
                    .leftJoin(qCart.cartAndProduct, qCartAndProduct)
                    .leftJoin(qCartAndProduct.product, qProduct)
                    .where(qmember.eq(member1))
                    .fetch();

            for (Product product : products) {
                MyPageSetDto setDto = new MyPageSetDto(product.getId(), memberId, product.getProductName(),
                        product.getProductImage(), product.getProductPrice(), product.getProductContent(),
                        product.getProductQuantity(), product.getCategory().getProductCategory());
                myPageSetDtos.add(setDto);
            }
            return myPageSetDtos;
        }
        List<Product> managerProducts = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.member.eq(member1))
                .fetch();
        for (Product managerProduct : managerProducts) {
            MyPageSetDto setDto = new MyPageSetDto(managerProduct.getId(), memberId, managerProduct.getProductName(),
                    managerProduct.getProductImage(), managerProduct.getProductPrice(), managerProduct.getProductContent(),
                    managerProduct.getProductQuantity(), managerProduct.getCategory().getProductCategory());
            myPageSetDtos.add(setDto);
        }
        return myPageSetDtos;

    }
}
