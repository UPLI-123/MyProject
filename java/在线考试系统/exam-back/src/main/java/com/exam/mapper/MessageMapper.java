package com.exam.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.Message;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Select("select id,id as temp_id,title,content,time from message order by id desc")
    @Results({
            @Result(property = "replays", column = "temp_id",many = @Many(select = "com.exam.mapper.ReplayMapper.findAllById"))
    })
    IPage<Message> findAll(Page page);

    @Select("select id,title,content,time from message where id = #{id}")
    @Results({
            @Result(property = "replays", column = "id",many = @Many(select = "com.exam.mapper.ReplayMapper.findAllById"))
    })
    Message findById(Integer id);

    @Delete("delete from message where id = #{id}")
    int delete(Integer id);

    @Update("update message set title = #{title}, content = #{content}, time = #{time} where " +
            "id = #{id}")
    int update(Message message);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into message(title, content, time,uid,power) values(#{title},#{content},#{time},#{uid},#{power})")
    int add(Message message);

    @Select("SELECT * FROM `message` where uid = #{v1} and power = #{v2}")
    List<Message> findAllMessage(@Param("v1") Integer uid, @Param("v2")Integer power) ;

    @Update("update message set power = #{v2} where id =#{v1} ")
    int updatePower(@Param("v1") Integer id,@Param("v2") Integer power) ;
}
