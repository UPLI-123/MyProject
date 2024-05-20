package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ExamProcess;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (ExamProcess)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-04 16:02:36
 */
@Mapper
public interface ExamProcessMapper {

    @Insert("insert into exam_process (ename,brief,count,uid) VALUES(#{ename},#{brief},#{count},#{uid})")
    public int add(ExamProcess examProcess) ;

    @Update("update exam_process set count = count +1 where id =#{v1}")
    public int updateCount(int id) ;

    @Delete("DELETE from exam_process WHERE id =#{v1}")
    public int delExamP(int id) ;

    @Select("SELECT * FROM `exam_process`")
    public List<ExamProcess> findAll() ;

    @Select("SELECT * from exam_process where uid =#{v1}")
    public IPage<ExamProcess> findByUid(@Param("page") Page page,@Param("v1") int id) ;

    @Select("SELECT * from exam_process where id =#{v1}")
    public ExamProcess findById(Integer id) ;

    @Update("update exam_process SET ename = #{ename} , brief = #{brief} where id =#{id}")
    public int updateInfo(ExamProcess examProcess) ;



}

