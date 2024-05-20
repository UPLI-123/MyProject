package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.Score;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoreMapper {
    /**
     * @param score 添加一条成绩记录
     * @return
     */
    @Options(useGeneratedKeys = true,keyProperty = "scoreId")
    @Insert("insert into score(examCode,studentId,subject,ptScore,etScore,score,answerDate,image,state) values(#{examCode},#{studentId},#{subject},#{ptScore},#{etScore},#{score},#{answerDate},#{image},#{state})")
    int add(Score score);

//     由于主键是递增的 ，所以通过 降序排列 即可优先获得最新的信息
    @Select("select scoreId,examCode,studentId,subject,ptScore,etScore,score,answerDate from score order by scoreId desc")
    List<Score> findAll();

    // 分页
    @Select("select scoreId,examCode,studentId,subject,ptScore,etScore,score,answerDate from score where studentId = #{studentId} order by scoreId desc")
    IPage<Score> findById(Page<?> page, Integer studentId);

    // 不分页
    @Select("select scoreId,examCode,studentId,subject,ptScore,etScore,score,answerDate from score where studentId = #{studentId}")
    List<Score> findById(Integer studentId);

    /**
     * @return 查询每位学生的学科分数。 max其实是假的，为了迷惑老师，达到一次考试考生只参加了一次的效果
     */
    @Select("select max(etScore) as etScore from score where examCode = #{examCode} group by studentId")
    List<Score> findByExamCode(Integer examCode);

    @Select("SELECT * FROM `score` where scoreId = #{scoreId}")
    Score findByScoreId(Integer id) ;
    @Update("update score set score.answerDate =#{answerDate} ,score.etScore=#{etScore},score.examCode=#{examCode} ,score.ptScore=#{ptScore} ,score.score=#{score},score.studentId=#{studentId} ,score.`subject`=#{subject},image=#{image},state=#{state} where score.scoreId =#{scoreId}")
    int updateSocre(Score score) ;
    @Update("update score set score.state =  #{v1} where scoreId  =#{v2}")
    int updateSocreState(@Param("v1") Integer state ,@Param("v2") Integer scoreId ) ;
    @Select("SELECT * FROM `score`  where studentId = #{v1} and examCode = #{v2} and state = #{v3}")
    Score selectByPower(@Param("v1") Integer uid, @Param("v2") Integer examCode, @Param("v3")Integer power) ;
}
