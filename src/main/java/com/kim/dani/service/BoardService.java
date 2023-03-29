package com.kim.dani.service;


import com.kim.dani.dtoGet.BoardAnswerGetDto;
import com.kim.dani.dtoGet.BoardGetDto;
import com.kim.dani.dtoSet.BoardSetDto;
import com.kim.dani.entity.Board;
import com.kim.dani.entity.Member;
import com.kim.dani.entity.QBoard;
import com.kim.dani.entity.QMember;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.BoardRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final JwtTokenV2 jwtTokenV2;
    private final QMember qmember = QMember.member;
    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final QBoard qBoard = QBoard.board;


    //보드 저장
    public boolean board(BoardGetDto boardGetDto , HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        Board board = new Board(null, boardGetDto.getTitle(), boardGetDto.getMessage(), null,null,member);

        boardRepository.save(board);

        return true;
    }


    //질문 리스트 (manager)
    public List<BoardSetDto> boardList(HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);
//        jwtTokenV2.tokenValidator(req);

        List<Board> boards = queryFactory
                .selectFrom(qBoard)
                .orderBy(qBoard.id.desc())
                .fetch();

        if (boards != null) {

            List<BoardSetDto> setDto = new ArrayList<>();
            for (Board board : boards) {
                BoardSetDto boardSetDto = new BoardSetDto(board.getId(), board.getTitle(), board.getMessage(),
                        board.getMember().getEmail(), board.getMember().getPhone(),board.getState(),board.getAnswer());
                setDto.add(boardSetDto);
            }
            return setDto;
        }
        return null;
    }


    //질문 리스트(customer)
    public List<BoardSetDto> boardListCustomer(HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Member member = queryFactory
                .selectFrom(qmember)
                .where(qmember.email.eq(getEmail))
                .fetchOne();

        List<Board> boards = queryFactory
                .selectFrom(qBoard)
                .where(qBoard.member.eq(member))
                .orderBy(qBoard.id.desc())
                .fetch();

        List<BoardSetDto> setDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardSetDto setDto = new BoardSetDto(board.getId(), board.getTitle(), board.getMessage(),
                    board.getMember().getEmail(), board.getMember().getPhone(), board.getState(), board.getAnswer());
            setDtos.add(setDto);
        }
        return setDtos;
    }



    //질문 보기 (manager)
    public BoardSetDto boardView(HttpServletRequest request,Long boardId, HttpServletResponse res) {

        Board board = queryFactory
                .selectFrom(qBoard)
                .where(qBoard.id.eq(boardId))
                .fetchOne();

        BoardSetDto boardSetDto = new BoardSetDto(board.getId(), board.getTitle(),
                board.getMessage(), board.getMember().getEmail(), board.getMember().getPhone(),board.getState(), board.getAnswer()) ;

        return boardSetDto;
    }


    //질문 보기(customer)

    public BoardSetDto boardViewCustomer(HttpServletRequest request,Long boardId, HttpServletResponse res) {

        Board board = queryFactory
                .selectFrom(qBoard)
                .where(qBoard.id.eq(boardId))
                .fetchOne();

        BoardSetDto boardSetDto = new BoardSetDto(board.getId(), board.getTitle(),
                board.getMessage(), board.getMember().getEmail(), board.getMember().getPhone(),board.getState(), board.getAnswer());

        return boardSetDto;
    }


    //답변하기 (manager)
    public boolean boardAnswer(HttpServletRequest req, BoardAnswerGetDto boardAnswerGetDto, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        if (getEmail.equals("master@master")) {

            Board board = queryFactory
                    .selectFrom(qBoard)
                    .where(qBoard.id.eq(boardAnswerGetDto.getBoardId()))
                    .fetchOne();

            board.setAnswer(boardAnswerGetDto.getAnswer());
            board.setState("답변 완료");
            boardRepository.save(board);
            return true;
        }
        return false;

    }




}
