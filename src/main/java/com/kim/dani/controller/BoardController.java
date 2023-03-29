package com.kim.dani.controller;


import com.kim.dani.dtoGet.BoardAnswerGetDto;
import com.kim.dani.dtoGet.BoardGetDto;
import com.kim.dani.dtoSet.BoardSetDto;
import com.kim.dani.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
@Tag(name = "board", description = "질문과 답변")
public class BoardController {


    private final  BoardService boardService;

    //질문 작성
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "문의사항 작성")
           ,@ApiResponse(responseCode = "400",description = "error code")
    })
    @Operation(summary = "질문 작성 페이지")
    @PostMapping("/save")
    public ResponseEntity board(@RequestBody BoardGetDto boardGetDto, HttpServletRequest req, HttpServletResponse res) {
        boolean answer = boardService.board(boardGetDto, req,res);

        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //질문 리스트(manager)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success code",content = @Content(schema = @Schema(implementation = BoardSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "질문 리스트 날열 페이지",description = "manager일 경우")
    @GetMapping("/list")
    public ResponseEntity boardList(HttpServletRequest request , HttpServletResponse res) {
        List<BoardSetDto> setDto = boardService.boardList(request,res);

        if (setDto!=null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 리스트(customer)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success code",content = @Content(schema = @Schema(implementation = BoardSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "질문 리스트 나열 페이지",description = "Customer일 경우")
    @GetMapping("/list/customer")
    public ResponseEntity boardListCustomer(HttpServletRequest req, HttpServletResponse res) {
        List<BoardSetDto> setDto = boardService.boardListCustomer(req,res);

        if (setDto!=null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 보기 (manager)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success code",content = @Content(schema = @Schema(implementation = BoardSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "질문 보기",description = "Manager일 경우")
    @GetMapping("/view/{boardId}")
    public ResponseEntity boardView(HttpServletRequest req,@PathVariable Long boardId, HttpServletResponse res) {
        BoardSetDto setDto = boardService.boardView(req,boardId,res);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //질문 보기 (Customer)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success code",content = @Content(schema = @Schema(implementation = BoardSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "질문 보기",description = "Customer일 경우")
    @GetMapping("/view/customer/{boardId}")
    public ResponseEntity boardViewCustomer(HttpServletRequest req,@PathVariable Long boardId, HttpServletResponse res) {
        BoardSetDto setDto = boardService.boardViewCustomer(req,boardId,res);

        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //답변 하기 (manager)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "seuucess code"),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "답변 저장" )
    @PostMapping("/view/answer")
    public ResponseEntity boardAnswer(HttpServletRequest req, @RequestBody BoardAnswerGetDto boardAnswerGetDto, HttpServletResponse res) {
        boolean getAnswer = boardService.boardAnswer(req, boardAnswerGetDto,res);
        if (getAnswer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }


}
