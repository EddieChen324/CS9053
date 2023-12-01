package com.community.dao;

import com.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param注解给参数取别名
    //如果只有一个参数，并且在if使用，必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
