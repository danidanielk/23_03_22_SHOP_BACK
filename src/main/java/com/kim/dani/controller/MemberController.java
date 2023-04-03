package com.kim.dani.controller;


import com.kim.dani.dtoGet.*;
import com.kim.dani.dtoSet.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member",description = "Member관련 API.")
public class MemberController {


    private final MemberService memberService;
    private final JwtTokenV2 jwtTokenV2;


    //회원가입
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "error code")
    })
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signin")
    public ResponseEntity signin(@Parameter @Valid @RequestBody MemberSigninGetDto memberSigninGetDto) {

        Boolean answer = memberService.signin(memberSigninGetDto);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //로그인
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = MemberLoginSetDto.class))),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity login(@Parameter @Valid @RequestBody MemberLoginGetDto memberLoginGetDto, HttpServletResponse res) {

        MemberLoginSetDto loginDto = memberService.login(memberLoginGetDto, res);
        if (loginDto != null) {
            return new ResponseEntity(loginDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //권한체크 Mypage에 MANAGER Page || CUSTOMER Page 생성

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AuthSetDto.class))),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "권한", description = "사용자 권한 체크")
    @GetMapping("/auth")
    public ResponseEntity auth(@Parameter HttpServletRequest req, HttpServletResponse res) {

        AuthSetDto authSetDto = memberService.auth(req, res);
        if (authSetDto == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(authSetDto, HttpStatus.OK);
    }


    //CUSTOMER = 추가한 상품 목록
    // MANAGER = 업로드한 상품 목록 , 수정 , 상품업로드
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = MyPageSetDto.class)))
            , @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "상품 리스트", description = "회원-장바구니 / 메니져-상품목록,수정,업로드")
    @PostMapping("/mypage/{memberId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity myPage(@PathVariable Long memberId, HttpServletRequest req) {
        List<MyPageSetDto> myPageSetDtos = memberService.myPage(memberId, req);
        if (myPageSetDtos != null) {
            return new ResponseEntity(myPageSetDtos, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = " 200", description = "success")
            , @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "장바구니 상품 삭제", description = "회원 장바구니 상품 삭제")
    @DeleteMapping("/delete/{cartProductId}")
    public ResponseEntity delete(@PathVariable Long cartProductId, HttpServletRequest req, HttpServletResponse res) {

        boolean answer = memberService.delete(cartProductId, req, res);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    // 상품 리스트에서 바로 구매시 주문서에 회원정보, 상품정보 자동입력
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BuySetDto.class))),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "주문서에 정보 자동입력", description = "상품리스트에서 구매시")
    @GetMapping("/buy/{productId}")
    public ResponseEntity buy(@PathVariable Long productId, HttpServletRequest req, HttpServletResponse res) {
        BuySetDto setDto = memberService.buy(productId, req, res);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    // 장바구니 리스트에서 구매시 주문서에 회원정보, 상품정보 자동입력
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BuySetDto.class))),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "주문서에 정보 자동입력", description = "장바구니에서 구매시")
    @GetMapping("/buycart/{cartProductId}")
    public ResponseEntity cartBuy(@PathVariable Long cartProductId, HttpServletRequest req, HttpServletResponse res) {
        BuySetDto setDto = memberService.cartBuy(cartProductId, req, res);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //주문하고 저장하기
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "error code")
    })
    @Operation(summary = "주문내역 DB저장")
    @PostMapping("/order")
    public ResponseEntity order(@Valid @RequestBody OrderGetDto orderGetDto, HttpServletRequest req, HttpServletResponse res) {
//        System.out.println(orderGetDto.getEmail());
        boolean answer = memberService.order(orderGetDto, req);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        String email = jwtTokenV2.tokenValidatiorAndGetEmail(req, res);
        System.out.println(email);
        return new ResponseEntity("재고부족", HttpStatus.BAD_REQUEST);
    }


    //나의 주문내역
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = OrderListCustomerSetDto.class))),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "나의 주문내역")
    @GetMapping("/orderlist")
    public ResponseEntity orderList(HttpServletRequest req, HttpServletResponse res) {

        List<OrderListCustomerSetDto> setDto = memberService.orderList(req, res);
        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity("주문내역이 없습니다", HttpStatus.NOT_FOUND);
    }

    //주문 취소
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "error code")
    })
    @Operation(summary = "주문 취소")
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity cancel(@PathVariable Long orderId, HttpServletRequest req, HttpServletResponse res) {
        boolean answer = memberService.cancel(req, orderId, res);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //회원정보수정 페이지 내정보확인
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = ModifyDataSetDto.class))),
            @ApiResponse(responseCode = "400", description = "error cdoe")
    })
    @Operation(summary = "회원정보 수정 페이지 ", description = "내 정보 확인")
    @GetMapping("/modify/data")
    public ResponseEntity modifyData(HttpServletRequest req, HttpServletResponse res) {

        ModifyDataSetDto setDto1 = memberService.modifyData(req, res);

        if (setDto1 != null) {
            return new ResponseEntity(setDto1, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    //회원정보 수정 버튼

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "error code")
    })
    @Operation(summary = "회원정보 수정")
    @PatchMapping("/modify")
    public ResponseEntity modifyInfo(@RequestBody ModifyDataGetDto modifyDataGetDto, HttpServletRequest req, HttpServletResponse res) {

        boolean answer = memberService.modifyInfo(modifyDataGetDto, req, res);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //회원탈퇴
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "error code")
    })
    @Operation(summary = "회원 삭제")
    @PostMapping("/modify/delete")
    public ResponseEntity modifyDelete(@RequestParam("memberId") Long memberId, @RequestParam("password") String password, HttpServletRequest req, HttpServletResponse res) {
        log.info("memberId 는 >> {}", memberId);
        log.info("password 는 >> {}", password);
        boolean answer = memberService.modifyDelete(memberId, req, password, res);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //즐겨찾기 상품 제거
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success"),
            @ApiResponse(responseCode = "404",description = "error")
    })
    @Operation(summary = "즐겨찾기 제거")
    @PostMapping("/bookmark/delete")
    public ResponseEntity bookmarkDelete(@RequestParam("bookmarkId") Long bookmarkId,HttpServletRequest req, HttpServletResponse res) {
        System.out.println("44444444444444444444444444444"+bookmarkId);
        boolean answer = memberService.bookmarkDelete(bookmarkId, req, res);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}