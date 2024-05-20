package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.FillQuestion;
import com.exam.vo.GAquestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

//填空题
@Mapper
public interface FillQuestionMapper {

    @Select("select * from fill_question where questionId in (select questionId from paper_manage where questionType = 3 and paperId = #{paperId})")
    List<FillQuestion> findByIdAndType(Integer paperId);

    @Select("select * from fill_question where eid = #{v1}")
    IPage<FillQuestion> findAll(@Param("page") Page page,@Param("v1") Integer eid);

    /**
     * 查询最后一条questionId
     * @return FillQuestion
     */
    @Select("select questionId from fill_question order by questionId desc limit 1")
    FillQuestion findOnlyQuestionId();

    @Options(useGeneratedKeys = true,keyProperty ="questionId" )
    @Insert("insert into fill_question(subject,question,answer,analysis,level,section,eid) values " +
            "(#{subject,},#{question},#{answer},#{analysis},#{level},#{section},#{eid})")
    int add(FillQuestion fillQuestion);

    @Select("select questionId from fill_question where subject = #{subject} order by rand() desc limit #{pageNo}")
    List<Integer> findBySubject(String subject,Integer pageNo);

    @Delete("DELETE from  fill_question where questionId = #{v}")
    void delFill(Integer id)  ;

    @Update("update fill_question set subject=#{subject},question =#{question},answer=#{answer},"+
    "analysis = #{analysis},level=#{level},section=#{section} where questionId =#{questionId}")
    void updteFill(FillQuestion fillQuestion) ;

//    遗传算法测试
    @Select("select * from fill_question where eid = #{v1}")
    List<GAquestion> getAll(@Param("v1") Integer eid);
}
