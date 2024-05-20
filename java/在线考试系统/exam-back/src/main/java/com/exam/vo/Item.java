package com.exam.vo;

import lombok.Data;

//使用一个临时变量用来存储临时的数据
@Data
public class Item {

    private String subject;

//    个体id
    private Integer paperId;

//  用来记录
    private Integer changeNumber;

    private Integer fillNumber;

    private Integer judgeNumber;

    private  Integer level ;  // 用来 记录 要求的难度系数

    private int[] value ;

}
