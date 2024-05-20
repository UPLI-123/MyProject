package com.exam.vo;

import lombok.Data;

/**
 * @projectName: exam
 * @package: com.exam.vo
 * @className: UserTemp
 * @author: lch
 * @description:  对两个表进行的封装
 * @date: 2022/4/17 19:22
 * @version: 1.0
 */
@Data
public class UserTemp {

    private Integer id;

    private String name;

    private String tel;

    private String email;

    private String password;
    //     添加一个code 方便传输值
    private String code ;

    private Integer examCode;

    private Integer studentId;

    private String subject;

    private Integer ptScore;

    private Integer etScore;

    private Integer score ;

    private Integer scoreId;

    private String answerDate;

    private String image ;

    private Integer state ;

}
