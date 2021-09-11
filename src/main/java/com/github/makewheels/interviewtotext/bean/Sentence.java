package com.github.makewheels.interviewtotext.bean;

import lombok.Data;

@Data
public class Sentence {
    private String startTimeString;
    private String endTimeString;
    private Integer person;
    private String content;
}
