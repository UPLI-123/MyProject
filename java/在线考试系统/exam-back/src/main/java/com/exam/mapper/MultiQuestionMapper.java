package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.MultiQuestion;
import com.exam.vo.GAquestion;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

//选择题
@Mapper
public interface MultiQuestionMapper {
    /**
     * select * from multiquestions where questionId in (
     * 	select questionId from papermanage where questionType = 1 and paperId = 1001
     * )
     */
    @Select("select * from multi_question where questionId in (select questionId from paper_manage where questionType = 1 and paperId = #{paperId})")
    List<MultiQuestion> findByIdAndType(Integer PaperId);

    @Select("select * from multi_question where eid = #{v1}")
    IPage<MultiQuestion> findAll(@Param("page") Page page,@Param("v1") Integer eid);

    /**
     * 查询最后一条记录的questionId
     * @return MultiQuestion
     */
    @Select("select questionId from multi_question order by questionId desc limit 1")
    MultiQuestion findOnlyQuestionId();

    @Options(useGeneratedKeys = true,keyProperty = "questionId")
    @Insert("insert into multi_question(subject,question,answerA,answerB,answerC,answerD,rightAnswer,analysis,section,level,eid) " +
            "values(#{subject},#{question},#{answerA},#{answerB},#{answerC},#{answerD},#{rightAnswer},#{analysis},#{section},#{level},#{eid})")
    int add(MultiQuestion multiQuestion);

    @Select("select questionId from multi_question  where subject =#{subject} order by rand() desc limit #{pageNo}")
    List<Integer> findBySubject(String subject,Integer pageNo);

    @Delete("DELETE from  multi_question where questionId = #{v} ")
    Integer removeMul(Integer id) ;

    @Update("update multi_question set subject=#{subject},question = #{question},answerA =#{answerA},answerB=#{answerB}," +
    "answerC=#{answerC},answerD=#{answerD},rightAnswer=#{rightAnswer},analysis=#{analysis},section=#{section},level=#{level} where questionId=#{questionId}")
    void updateMul(MultiQuestion multiQuestion) ;
//    遗传算法测试 分界线
    @Select("select * from multi_question where eid = #{v1}")
    List<GAquestion> getAll(Integer eid) ;



}
