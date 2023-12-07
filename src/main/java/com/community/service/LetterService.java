package com.community.service;

import com.community.dao.LetterMapper;
import com.community.entity.Letter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LetterService {
    @Autowired
    private LetterMapper letterMapper;

    public List<Letter> findLetters(int userId, int offset, int limit) {
        return letterMapper.selectLetters(userId, offset, limit);
    }

    public int getLetterCountById(int fromId, int toId) {
        return letterMapper.getLetterCountById(fromId, toId);
    }

    //有几条通话
    public int getLetterCount(int userId) {
        return letterMapper.getLetterCount(userId);
    }

    public List<Letter> findLetterDetails(int fromId, int toId, int offset, int limit) {
        return letterMapper.selectLetterDetails(fromId, toId, offset, limit);
    }

    public  Letter getLetterById(int id) {
        return letterMapper.selectLetterById(id);
    }


    public int getTotalLetter(int id, String conversationId) {
        return letterMapper.getTotalLetter(id, conversationId);
    }

    public Letter getLatestNotice(int id, String conversationId) {
        return letterMapper.selectLatestNotice(id, conversationId);
    }

    public List<Letter> getNotices(int id, String conversationId, int offset, int limit) {
        return letterMapper.selectNotices(id, conversationId, offset, limit);
    }

}
