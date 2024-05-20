package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.JudgeQuestion;
import com.exam.vo.GAquestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

//判断题

@Mapper
public interface JudgeQuestionMapper {

    @Select("select * from judge_question where questionId in (select questionId from paper_manage where questionType = 2 and paperId = #{paperId})")
    List<JudgeQuestion> findByIdAndType(Integer paperId);

    @Select("select * from judge_question where eid = #{v1}")
    IPage<JudgeQuestion> findAll(@Param("page") Page page,@Param("v1") Integer eid);

    /**
     * 查询最后一条记录的questionId
     * @return JudgeQuestion
     */
    @Select("select questionId from judge_question order by questionId desc limit 1")
    JudgeQuestion findOnlyQuestionId();

    @Insert("insert into judge_question(subject,question,answer,analysis,level,section,eid) values " +
            "(#{subject},#{question},#{answer},#{analysis},#{level},#{section},#{eid})")
    int add(JudgeQuestion judgeQuestion);

    @Select("select questionId from judge_question  where subject=#{subject}  order by rand() desc limit #{pageNo}")
    List<Integer> findBySubject(String subject,Integer pageNo);

    @Delete("DELETE from  judge_question where questionId = #{v}")
    void removeJud(Integer id) ;

    @Update("update judge_question set subject =#{subject},question=#{question},answer=#{answer},analysis=#{analysis},"+
    "level = #{level},section = #{section} where questionId = #{questionId}")
    void updteJud(JudgeQuestion judgeQuestion)  ;
//     遗传算法测试

    @Select("select * from judge_question where eid = #{v1}")
    List<GAquestion> getAll(@Param("v1") Integer eid);
}
