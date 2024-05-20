package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamManage;
import org.apache.ibatis.annotations.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@Mapper
public interface ExamManageMapper {

//     不带分页的个人创建试卷查询
    @Select("select * from exam_manage where createId = #{v1}")
    List<ExamManage> findAll2(@Param("v1") Integer id);
//     带有分页的查询操作
    @Select("select * from exam_manage where createId = #{id}")
    IPage<ExamManage> findAll1(@Param("page") Page page,@Param("id") Integer id);

    @Select("select * from exam_manage where examCode = #{examCode}")
    ExamManage findById(Integer examCode);

    @Delete("delete from exam_manage where examCode = #{examCode}")
    int delete(Integer examCode);

    @Update("update exam_manage set description = #{description}," +
            "examDate = #{examDate},totalTime = #{totalTime}," +
            "btime = #{btime},etime = #{etime} where examCode = #{examCode}")
    int update(ExamManage exammanage);

    @Options(useGeneratedKeys = true,keyProperty = "examCode")
    @Insert("insert into exam_manage(description,source,paperId,examDate,totalTime,grade,term,major,institute,totalScore,type,tips,isroot,fatherid,ismedia,btime,etime,ispublic,createId)" +
            " values(#{description},#{source},#{paperId},#{examDate},#{totalTime},#{grade},#{term},#{major},#{institute},#{totalScore},#{type},#{tips},#{isroot},#{fatherid},#{ismedia},#{btime},#{etime},#{ispublic},#{createId})")
    int add(ExamManage exammanage) ;

    /**
     * 查询最后一条记录的paperId,返回给前端达到自增效果
     * @return paperId
     */
    @Select("select paperId from exam_manage order by paperId desc limit 1")
    ExamManage findOnlyPaperId();

//    查询所有 公共权限的试卷列表
    @Select("SELECT * FROM exam_manage WHERE exam_manage.ispublic = #{id1}")
    IPage<ExamManage> findAll(@Param("page") Page page,@Param("id1") int id);

////    根据 paperId进行查询
    @Select("SELECT * FROM `exam_manage` where paperId = #{v1}")
    ExamManage findByPid(Integer pid) ;

    @Select("SELECT * from exam_manage where exam_manage.fatherid=#{v2}")
    public List<ExamManage> findByFid(Integer id) ;

    @Update("update exam_manage set ispublic = #{ispublic} where examCode =#{examCode}")
    public int updatePublic(@Param("ispublic") int ispublic, @Param("examCode") int examCode)  ;

//    @Select("SELECT e.*,s.score from  exam_manage e , score s where e.examCode = s.examCode and s.state = 0 and s.studentId = #{v1}")
    public List<ExamManage> getAllByuid(int id) ;

    @Select("SELECT e.*, s.score from  exam_manage e , score s where e.examCode = s.examCode and s.state = 0  and s.studentId = #{id} and examDate = #{time}")
    public List<ExamManage> getAllByTime(@Param("id") int id,@Param("time") String time) ;


}
