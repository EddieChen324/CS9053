package com.community.dao;

import com.community.entity.DiscussPost;
import com.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param注解给参数取别名
    //如果只有一个参数，并且在if使用，必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);


    DiscussPost selectById(int id);

    int insertDiscussPost(DiscussPost discussPost);

    int updateCommentCount(int id, int commentCount);
}
