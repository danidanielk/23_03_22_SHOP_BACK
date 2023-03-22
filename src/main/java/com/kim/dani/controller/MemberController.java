package com.kim.dani.controller;


import com.kim.dani.dtoGet.MemberLoginGetDto;
import com.kim.dani.dtoGet.MemberSigninGetDto;
import com.kim.dani.dtoSet.AuthSetDto;
import com.kim.dani.dtoSet.MemberLoginSetDto;
import com.kim.dani.entity.Auth;
import com.kim.dani.entity.Member;
import com.kim.dani.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member",description = "Member관련 API.")
public class MemberController {



    private final MemberService memberService;


//    @ApiResponse(value={
//    @ApiResponse(responseCode = "400",description = "bad request operation",content = @Content(schema = @Schema(implementation = Member.class))),
//    })

    //회원가입
    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Member.class)))
    @Operation(summary = "회원가입",description = "회원가입")
    @PostMapping("/signin")
    public ResponseEntity signin(@Parameter @Valid @RequestBody MemberSigninGetDto memberSigninGetDto){

        Boolean answer = memberService.signin(memberSigninGetDto);
        if (answer){
        return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //로그인
    @ApiResponse(responseCode = "200" , description = "success",content =@Content(schema =@Schema(implementation = Member.class)))
    @Operation(summary = "로그인",description = "로그인")
    @PostMapping("/login")
    public ResponseEntity login(@Parameter @Valid @RequestBody MemberLoginGetDto memberLoginGetDto, HttpServletResponse res) {

        MemberLoginSetDto loginDto = memberService.login(memberLoginGetDto, res);
        if(loginDto != null){
            return new ResponseEntity(loginDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //권한체크 후 MANAGER 은 관리자탭 생성
    @ApiResponse(responseCode = "200", description = "success",content = @Content(schema = @Schema(implementation = AuthSetDto.class)))
    @Operation(summary = "권한",description = "사용자 권한 체크 후 관리자탭생성")
    @GetMapping("/auth")
    public ResponseEntity auth(@Parameter HttpServletRequest req) {

        AuthSetDto authSetDto = memberService.auth(req);
        if (authSetDto == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(authSetDto, HttpStatus.OK);
    }


//    @ApiResponse(responseCode = )

//    private final TestTeamRepository teamRepository;
//    private final TestMemberRepository memberRepository;
//
//
//    @PostMapping("/test")
//    public String test1(@RequestBody TestDto testDto) {
//        Team team = new Team();
//        team.setName(testDto.getTeamname());
//        teamRepository.save(team);
//        Member member = new Member();
//        member.setName(testDto.getMembername());
//        member.setTeam(team);
//        memberRepository.save(member);
//        return "success";
//    }
//
//
//    //전체 멤버 list로 조회
//    @GetMapping("/test1")
//    public ResponseEntity getAllMember(){
//       List<Member> getMember= queryFactory
//                .selectFrom(member).fetch();
//        return new ResponseEntity(getMember, HttpStatus.OK);
//    }
//
//    //특정 Team 의 특정 Member조회
//    @GetMapping("/test2/{teamid}/{memberid}")
//    public Member getMember(@PathVariable Long teamid , @PathVariable Long memberid) {
//        Member getMember = queryFactory
//                .selectFrom(member)
//                .leftJoin(member.team, team)
//                .where(team.id.eq(teamid),member.id.eq(memberid))
//                .fetchOne();
//        return getMember;
//    }
//
//
//    //특정 Member를 조회
//    @GetMapping("/test3/{id}")
//    public Member getMember2(@PathVariable Long id){
//        Member getmember = queryFactory
//                .selectFrom(member)
//                .where(member.id.eq(id))
//                .fetchOne();
//        return getmember;
//    }
}
