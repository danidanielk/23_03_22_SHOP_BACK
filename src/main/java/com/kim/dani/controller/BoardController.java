package com.kim.dani.controller;


import com.kim.dani.dtoGet.BoardAnswerGetDto;
import com.kim.dani.dtoGet.BoardGetDto;
import com.kim.dani.dtoSet.BoardSetDto;
import com.kim.dani.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
@Tag(name = "board", description = "질문과 답변")
public class BoardController {


    private final  BoardService boardService;

    //질문 작성
    @PostMapping("/save")
    public ResponseEntity board(@RequestBody BoardGetDto boardGetDto, HttpServletRequest req) {
        boolean answer = boardService.board(boardGetDto, req);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //질문 리스트(manager)
    @GetMapping("/list")
    public ResponseEntity boardList(HttpServletRequest request) {
        List<BoardSetDto> setDto = boardService.boardList(request);

        if (setDto!=null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 리스트(customer)
    @GetMapping("/list/customer")
    public ResponseEntity boardListCustomer(HttpServletRequest req) {
        List<BoardSetDto> setDto = boardService.boardListCustomer(req);

        if (setDto!=null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 보기 (manager)
    @GetMapping("/view/{boardId}")
    public ResponseEntity boardView(HttpServletRequest req,@PathVariable Long boardId) {
        BoardSetDto setDto = boardService.boardView(req,boardId);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 보기 (Customer)
    @GetMapping("/view/customer/{boardId}")
    public ResponseEntity boardViewCustomer(HttpServletRequest req,@PathVariable Long boardId) {
        BoardSetDto setDto = boardService.boardViewCustomer(req,boardId);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //답변 하기 (manager)
    @PostMapping("/view/answer")
    public ResponseEntity boardAnswer(HttpServletRequest req, @RequestBody BoardAnswerGetDto boardAnswerGetDto) {
        boolean getAnswer = boardService.boardAnswer(req, boardAnswerGetDto);
        if (getAnswer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }


}
