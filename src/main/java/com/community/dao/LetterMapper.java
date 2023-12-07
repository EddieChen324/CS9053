package com.community.dao;

import com.community.entity.Letter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LetterMapper {

    //找到哪些人私信
    List<Letter> selectLetters(int userId, int offset, int limit);

    //找到notice
    List<Letter> selectNotices(int userId, String conversationId, int offset, int limit);

    Letter selectLetterById(int id);

    List<Letter> selectLetterDetails(int fromId, int toId, int offset, int limit);

    //当前两个用户的对话总数
    int getLetterCountById(int fromId, int toId);

    int getLetterCount(int userId);

    //根据conversation_id来选择是notice总数 还是letter总数 还是不同notice总数
    int getTotalLetter(int userId, String conversationId);

    Letter selectLatestNotice(int userId, String conversationId);
}
