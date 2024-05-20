package com.exam.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ExamManage {
    private Integer examCode;

    private String description;

    private String source;

    private Integer paperId;

    private String examDate;

    private Integer totalTime;

    private String grade;

    private String term;

    private String major;

    private String institute;

    private Integer totalScore;

    private String type;

    private String tips;

    private Integer isroot ;

    private  Integer fatherid ;

    private Integer ismedia ;

    private String btime ;

    private String etime ;

    private Integer ispublic ;

    private Integer createId ;

    private Double score ;

}