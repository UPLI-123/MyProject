package com.exam.mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.EaxmStore;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (EaxmStore)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-14 10:08:13
 */
@Mapper
public interface EaxmStoreMapper{

    @Insert("insert into eaxm_store(id,uid,name,count,gtime) VALUES (null,#{uid},#{name},0,#{gtime})")
    public void addExamStore(EaxmStore eaxmStore) ;

    @Select("SELECT *from eaxm_store where uid =#{v1}")
    public IPage<EaxmStore> selectAll(@Param("page")Page page,@Param("v1") Integer id) ;


    @Select("select * from eaxm_store where id = #{v}")
    public EaxmStore selectById(Integer id) ;

    @Update("update eaxm_store set count = #{count}, gtime = #{gtime} WHERE id = #{id} ")
    public Integer updateStore(EaxmStore store) ;

    // 数量更新减一
    @Update("update eaxm_store set count = count-1 , gtime = #{gtime} WHERE id = #{id}")
    public Integer updateCount(EaxmStore store) ;

    @Delete("DELETE  from eaxm_store where id = #{v1}")
    public int delById(Integer id) ;

    @Select("SELECT *from eaxm_store where uid =#{v1}")
    public List<EaxmStore> findByUid(Integer uid) ;
}

