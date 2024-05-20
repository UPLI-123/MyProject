package com.exam.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @projectName: exam
 * @package: com.exam.vo
 * @className: GAquestion
 * @author: lch
 * @description: 本类 用来封装 遗传算法题目的类 ,将三个类的属性都 封装到一起，从而
 * 解决 返回值不一致的问题
 * @date: 2022/5/2 19:48
 * @version: 1.0
 */
@Data
public class GAquestion {

    private Integer questionId;

    private String subject;

    private String section;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private String question;

    private String level;

    private String rightAnswer;

    private String analysis; //题目解析

    private Integer score;

    private Integer eid  ;

    private String answer;

}
