package com.community.Controller;


import com.alibaba.fastjson.JSONObject;
import com.community.entity.Letter;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.LetterService;
import com.community.service.UserService;
import com.community.util.CommunityConstant;
import com.community.util.HostHolder;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class LetterController implements CommunityConstant {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LetterService letterService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/{id}/letter", method = RequestMethod.GET)
    public String getLetter(@PathVariable("id") int id, Model model, Page page) {
        User user = hostHolder.getUser();
        page.setLimit(10);
        List<Letter> list = letterService.findLetters(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (list != null) {
            for (Letter letter : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", letter);
                User sender = userService.findUserById(letter.getFromId());
                map.put("sender", sender);
                int count = letterService.getLetterCountById(sender.getId(), user.getId());
                map.put("count", count);
                letters.add(map);
            }
        }
        int totalConversation = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_LETTER);
        //total指获取所有notice
        int totalNotice = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_NOTICE);
        page.setRows(letterService.getLetterCount(user.getId()));
        page.setPath("/" + id + "/letter");
        model.addAttribute("letters", letters);
        model.addAttribute("user", user);
        model.addAttribute("conversationCount", totalConversation);
        model.addAttribute("noticeCount", totalNotice);
        return "/site/letter";
    }

    @RequestMapping(path = "/{id}/letter/details/{letterId}", method = RequestMethod.GET)
    public String getLetterDetails(@PathVariable("id") int id, @PathVariable("letterId") int letterId, Model model, Page page) {
        Letter letter = letterService.getLetterById(letterId);
        User sender = userService.findUserById(letter.getFromId());
        User receiver = userService.findUserById(letter.getToId());
        page.setLimit(4);
        List<Letter> list = letterService.findLetterDetails(sender.getId(), receiver.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (list != null) {
            for (Letter currentLetter : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", currentLetter);
                letters.add(map);
                User currentSender = userService.findUserById(currentLetter.getFromId());
                map.put("currentSender", currentSender);
            }
        }
        page.setPath("/" + id + "/letter/details/" + letterId);
        page.setRows(letterService.getLetterCountById(sender.getId(), receiver.getId()));
        model.addAttribute("sender", sender);
        model.addAttribute("letters", letters);
        model.addAttribute("userId", id);
        return "/site/letter-detail";
    }

    @RequestMapping(path = "/{id}/letter/notice", method = RequestMethod.GET)
    public String getNotice(@PathVariable("id") int id, Model model) {
        User user = hostHolder.getUser();
        int totalConversation = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_LETTER);
        int totalNotice = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_NOTICE);
        int likeNoticeCount = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_LIKE);
        int commentCount = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_COMMENT);
        int followCount = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_FOLLOW);
        Letter latestLike = letterService.getLatestNotice(user.getId(), CONVERSATION_ID_LIKE);
        Letter latestComment = letterService.getLatestNotice(user.getId(), CONVERSATION_ID_COMMENT);
        Letter latestFollow = letterService.getLatestNotice(user.getId(), CONVERSATION_ID_FOLLOW);
        User liker = new User();
        User commenter = new User();
        User follower = new User();
        if (latestLike != null) {
            liker = userService.findUserById(latestLike.getFromId());
        }
        if (latestComment != null) {
            commenter = userService.findUserById(latestComment.getFromId());
        }
        if (latestFollow != null) {
            follower = userService.findUserById(latestFollow.getFromId());
        }
//        User liker = userService.findUserById(latestLike.getFromId());
//        User commenter = userService.findUserById(latestComment.getFromId());
//        User follower = userService.findUserById(latestFollow.getFromId());
        model.addAttribute("user", user);
        model.addAttribute("conversationCount", totalConversation);
        model.addAttribute("noticeCount", totalNotice);
        model.addAttribute("likeCount", likeNoticeCount);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("followCount", followCount);
        model.addAttribute("latestLike", latestLike);
        model.addAttribute("latestComment", latestComment);
        model.addAttribute("latestFollow", latestFollow);
        model.addAttribute("liker", liker);
        model.addAttribute("commenter", commenter);
        model.addAttribute("follower", follower);
        return "site/notice";
    }

    @RequestMapping(path = "/{id}/letter/notice/detail/{conversationId}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("id") int id, @PathVariable("conversationId") String conversationId, Model model, Page page) {
        page.setPath("/" + id + "/letter/notice/detail/" + conversationId);
        page.setRows(letterService.getTotalLetter(id, conversationId));
        page.setLimit(5);
        List<Letter> list = letterService.getNotices(id, conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeDetails = new ArrayList<>();
        if (list != null) {
            for (Letter letter : list) {
                Map<String, Object> map = new HashMap<>();
                //获取帖子id （方便超链接转入） 以及发帖时间即可
                JSONObject jsonObject = JSONObject.parseObject(letter.getContent());
                if (!Objects.equals(conversationId, CONVERSATION_ID_FOLLOW)) {
                    int postId = jsonObject.getIntValue("postId");
                    map.put("postId", postId);
                }
                map.put("conversationId", conversationId);
                map.put("createTime", letter.getCreateTime());
                User sender = userService.findUserById(letter.getFromId());
                map.put("sender", sender);
                noticeDetails.add(map);
            }
        }
        model.addAttribute("userId", id);
        model.addAttribute("noticeDetails", noticeDetails);
        return "/site/notice-detail";
    }
}
