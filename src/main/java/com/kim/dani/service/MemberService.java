package com.kim.dani.service;


import com.kim.dani.dtoGet.*;
import com.kim.dani.dtoSet.*;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.*;
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
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final CartAndProductRepository cartAndProductRepository;
    private final BoardRepository boardRepository;
    private final QMember qmember = QMember.member;
    private final QCart qCart = QCart.cart;
    private final QCartAndProduct qCartAndProduct = QCartAndProduct.cartAndProduct;
    private final QProduct qProduct = QProduct.product;
    private final QCartProduct qCartProduct = QCartProduct.cartProduct;
    private final QBuyer qBuyer = QBuyer.buyer;
    private final QBoard qBoard = QBoard.board;
    private final JwtTokenV2 jwtTokenV2;


    //회원가입
    public boolean signin (MemberSigninGetDto memberSigninGetDto){
        String pw = memberSigninGetDto.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderPassword = passwordEncoder.encode(pw);
        if(memberSigninGetDto.getEmail().equals("master@master")){
        Member member1 = new Member(null, null,memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(),
                encoderPassword, Auth.MANAGER,null,null,null,null,null);
            try {
            memberRepository.save(member1);
            return true;
            }catch (Exception e){
            return false;
            }
        }
        Member member1 = new Member(null, null,memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(),
                encoderPassword, Auth.CUSTOMER,null,null,null,null,null);
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
                .where(qmember.email.eq(memberLoginGetDto.getEmail()))
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
    public AuthSetDto auth(HttpServletRequest req, HttpServletResponse res){
        String email = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);
        if (email ==null){
            return null;
        }
        Member member1 = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(email))
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

//            List<CartProduct> cartProducts = queryFactory

            List<CartProduct> products = queryFactory
                    .select(qCartProduct)
                    .from(qmember)
                    .leftJoin(qmember.cart, qCart)
                    .leftJoin(qCart.cartAndProduct, qCartAndProduct)
                    .leftJoin(qCartAndProduct.cartProduct, qCartProduct)
                    .where(qmember.eq(member1))
                    .fetch();

            for (CartProduct product : products) {
                MyPageSetDto setDto = new MyPageSetDto(product.getProductId(), product.getId(), memberId, product.getProductName(),
                        product.getProductImage(), product.getProductPrice(), product.getProductContent(),
                        null,product.getProductQuantity(), product.getCategory());
                myPageSetDtos.add(setDto);
            }
            return myPageSetDtos;
        }
        List<Product> managerProducts = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.member.eq(member1))
                .fetch();
        for (Product managerProduct : managerProducts) {
            MyPageSetDto setDto = new MyPageSetDto(managerProduct.getId(),null, memberId, managerProduct.getProductName(),
                    managerProduct.getProductImage(), managerProduct.getProductPrice(), managerProduct.getProductContent(),
                    managerProduct.getProductQuantity(),null, managerProduct.getCategory().getProductCategory());
            myPageSetDtos.add(setDto);
        }
        return myPageSetDtos;

    }


    //회원 myPage cart 선택 상품 삭제.
    public boolean delete (Long cartProductId , HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();


        CartAndProduct cartAndProduct = queryFactory
                .selectFrom(qCartAndProduct)
                .where(qCartAndProduct.cart.member.eq(member))
                .where(qCartAndProduct.cartProduct.id.eq(cartProductId))
                .fetchOne();


        cartAndProductRepository.delete(cartAndProduct);
        return true;


    }

    //장바구니에 담지않고 바로 주문
    public BuySetDto buy(Long productId , HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        BuySetDto setDto = new BuySetDto(productId, product.getProductName(), product.getProductImage(),
                product.getProductPrice(), product.getProductContent(), product.getProductQuantity(),
                product.getCategory().getProductCategory(), member.getId(), member.getEmail(), member.getPhone());

        return setDto;
    }

    //장바구니에 담아서 주문
    public BuySetDto cartBuy(Long cartProductId , HttpServletRequest req, HttpServletResponse res) {
        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        CartProduct cartProduct = queryFactory
                .selectFrom(qCartProduct)
                .where(qCartProduct.id.eq(cartProductId))
                .fetchOne();

        BuySetDto setDto = new BuySetDto(cartProductId, cartProduct.getProductName(), cartProduct.getProductImage(),
                cartProduct.getProductPrice(), cartProduct.getProductContent(), cartProduct.getProductQuantity(),
                cartProduct.getCategory(), member.getId(), member.getEmail(), member.getPhone());

        return setDto;
    }


    //주문내역 저장
    public boolean order(OrderGetDto orderGetDto, HttpServletRequest req) {

//        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        String getEmail = orderGetDto.getEmail();
        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.productName.eq(orderGetDto.getProductName()))
                .fetchOne();

        if (product.getProductQuantity() >= orderGetDto.getProductQuantity()) {
        product.setProductQuantity(product.getProductQuantity() - orderGetDto.getProductQuantity());
        productRepository.save(product);

        Buyer buyer = new Buyer(null, orderGetDto.getAddress(), orderGetDto.getEmail(), orderGetDto.getPhone(),
                orderGetDto.getProductName(), orderGetDto.getProductPrice(), orderGetDto.getProductQuantity(),
                orderGetDto.getMessage(), member);

        buyerRepository.save(buyer);
        return true;
        }
        return false;

    }


    // 주문취소 (수량 반환)
    public boolean cancel(HttpServletRequest req,Long orderId, HttpServletResponse res) {

        String getEmail= jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        Buyer buyer = queryFactory
                .selectFrom(qBuyer)
                .where(qBuyer.id.eq(orderId))
                .fetchOne();

        String productName = buyer.getProductName();

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.productName.eq(productName))
                .fetchOne();

        Long productQuantity = buyer.getProductQuantity();

        product.setProductQuantity(productQuantity + product.getProductQuantity());
        productRepository.save(product);

        buyerRepository.delete(buyer);
        return true;

    }


    //나의 주문 내역
    public List<OrderListCustomerSetDto> orderList(HttpServletRequest request, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(request,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        List<Buyer> buyers = queryFactory
                .selectFrom(qBuyer)
                .where(qBuyer.member.eq(member))
                .fetch();

        List<OrderListCustomerSetDto> setDtos = new ArrayList<>();
        for (Buyer buyer : buyers) {
            OrderListCustomerSetDto setDto = new OrderListCustomerSetDto(buyer.getId(), buyer.getAddress(),
                    buyer.getEmail(), buyer.getPhone(), buyer.getProductName(), buyer.getProductPrice(),
                    buyer.getProductQuantity(), buyer.getMessage());
            setDtos.add(setDto);
        }
        return setDtos;
    }


    //회원정보 수정페이지 기본데이터
    public ModifyDataSetDto modifyData(HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        if (member != null) {
            ModifyDataSetDto modifyDataSetDto = new ModifyDataSetDto(member.getId(), member.getEmail(), member.getPhone());
            return modifyDataSetDto;
        }
        return null;
    }



    //회원 비밀번호 수정
    public boolean modifyInfo(ModifyDataGetDto modifyDataGetDto,HttpServletRequest req, HttpServletResponse res) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        if (modifyDataGetDto.getCurrentPassword() != null || modifyDataGetDto.getCurrentPassword() != "" &&
            modifyDataGetDto.getPassword() != null || modifyDataGetDto.getPassword() !="" ) {
        String currentPw = modifyDataGetDto.getCurrentPassword();
        String hashedPw = member.getPassword();
        boolean pwd = passwordEncoder.matches(currentPw, hashedPw);

        if (pwd) {
            String pw = passwordEncoder.encode(modifyDataGetDto.getPassword());
//            member.setPhone(modifyDataGetDto.getPhone());
            member.setPassword(pw);
            memberRepository.save(member);
            return true;
        }
        }
        return false;
    }


    //회원 삭제
    public boolean modifyDelete(Long memberId,HttpServletRequest req,String password, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean pw = passwordEncoder.matches(password, member.getPassword());

        if (member.getId() == memberId && pw) {
            memberRepository.delete(member);
            return true;
        }
        return false;
    }

}
