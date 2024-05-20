package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.FillQuestion;
import com.exam.entity.JudgeQuestion;
import com.exam.entity.MultiQuestion;
import com.exam.entity.Myerror;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Myerror)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-10 14:54:32
 */
@Mapper
public interface MyerrorMapper{

//     首先查询某一用户当前试卷所有的选择题错题
    @Select("SELECT m.* ,me.id from multi_question m , myerror me , paper_manage p where me.paperId = p.paperId and m.questionId = me.questionId and uid =#{v1} and type =1 and p.paperId =#{v2}")
    public List<MultiQuestion> findErrorMul(@Param("v1") int uid,@Param("v2") int pid) ;

//     首先查询某一用户当前试卷所有的判断题错题
    @Select("SELECT m.* ,me.id from judge_question m , myerror me , paper_manage p where me.paperId = p.paperId and m.questionId = me.questionId and uid =1 and type =2 and p.paperId =#{v2}")
    public List<JudgeQuestion> findErrorJud(@Param("v1") int uid,@Param("v2") int pid) ;

//     首先查询某一用户当前试卷所有的填空题错题
    @Select("SELECT m.*,me.id from fill_question m , myerror me , paper_manage p where me.paperId = p.paperId and m.questionId = me.questionId and uid =#{v1} and type =3 and p.paperId =#{v2}")
    public List<FillQuestion> findErrorFill(@Param("v1") int uid,@Param("v2") int pid) ;

//   增加用户
    @Insert("INSERT into myerror(type,paperId,examCode,uid,questionId) VALUES(#{type},#{paperId},#{examCode},#{uid},#{questionId})")
    public int addMyError(Myerror myerror) ;
//    删除用户
    @Delete("DELETE from myerror WHERE id = #{v1}")
    public int removeMyError(int id) ;
}

