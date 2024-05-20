package com.exam.mapper;

import com.exam.entity.FillQuestion;
import com.exam.entity.JudgeQuestion;
import com.exam.entity.MultiQuestion;
import com.exam.entity.PaperManage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaperMapper {
    @Select("select paperId, questionType,questionId,score from paper_manage")
    List<PaperManage> findAll();
    @Select("select paperId, questionType,questionId from paper_manage where paperId = #{paperId}")
    List<PaperManage> findById(Integer paperId);
    @Insert("insert into paper_manage(paperId,questionType,questionId,score) values " +
            "(#{paperId},#{questionType},#{questionId},#{score})")
    int add(PaperManage paperManage);

    @Select("SELECT m.* from paper_manage p , multi_question m where  p.questionId = m.questionId and p.questionType =1 and p.paperId = #{v}")
    public List<MultiQuestion> findMulByPid(Integer id) ;

    @Select("SELECT m.* from paper_manage p , judge_question m where  p.questionId = m.questionId and p.questionType =2 and p.paperId = #{v}")
    public List<JudgeQuestion> findJudByPid(Integer id) ;

    @Select("SELECT m.* from paper_manage p , fill_question m where  p.questionId = m.questionId and p.questionType =3 and p.paperId = #{v}")
    public List<FillQuestion> findFillByPid(Integer id) ;
}
