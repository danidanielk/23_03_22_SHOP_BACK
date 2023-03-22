package com.kim.dani.member;

import com.kim.dani.dtoGet.MemberSigninGetDto;
import com.kim.dani.entity.Member;
import com.kim.dani.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Test 멤버 저장")
    void saveMember(){

        //given
        MemberSigninGetDto memberSigninGetDto = new MemberSigninGetDto();
        Member member = new Member(null, memberSigninGetDto.getEmail(), memberSigninGetDto.getPhone(), memberSigninGetDto.getPassword(),null);

        //when
        Member saveMember = memberRepository.save(member);


        //then
        assertThat(saveMember.getId()).isNotNull();
        assertThat(saveMember.getEmail()).isEqualTo(memberSigninGetDto.getEmail());
        assertThat(saveMember.getPhone()).isEqualTo(memberSigninGetDto.getPhone());
        assertThat(saveMember.getPassword()).isEqualTo(memberSigninGetDto.getPassword());

    }

    @Test
    @DisplayName("Login Test")
    void login(){

        //given

        //when

        //then
    }
}
