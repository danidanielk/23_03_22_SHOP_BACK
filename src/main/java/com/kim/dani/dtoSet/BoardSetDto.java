package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSetDto {

    private Long boardId;

    private String title;

    private String message;

    private String email;

    private String phone;

    private String state;

    private String answer;

}


