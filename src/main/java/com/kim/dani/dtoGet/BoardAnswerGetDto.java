package com.kim.dani.dtoGet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardAnswerGetDto {

    private Long boardId;

    private String answer;
}
