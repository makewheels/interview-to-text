package com.github.makewheels.interviewtotext;

import lombok.Data;

@Data
public class Sentence {
    private String startTimeString;
    private String endTimeString;
    private Integer person;
    private String content;
}
