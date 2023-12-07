package com.community.Controller;

import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostService;
import com.community.service.LetterService;
import com.community.service.UserService;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/post")
public class PostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LetterService letterService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = "", method = RequestMethod.GET)
    //@PathVariable("id")注解使得Spring能够从URL中提取帖子ID，并将其作为方法参数。
    public String getPostDetail(Model model, @RequestParam("id") int id, Page page) {
        //找到帖子
        DiscussPost post = discussPostService.findPostById(id);
        //帖子的评论数
        page.setRows(post.getCommentCount());
        page.setPath("");
        page.setLimit(5);
        //帖子的发帖人
        User poster = userService.findUserById(post.getUserId());
        // 当前post所有comment构成的list
        List<Comment> list = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 记录所有comment相关的视图信息
        List<Map<String, Object>> comments = new ArrayList<>();
        //将评论和评论者存入model
        if (list != null) {
            for (Comment comment : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                User commenter = userService.findUserById(comment.getUserId());
                map.put("commenter", commenter);
                //查询评论的回复
                List<Comment> replies = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> repliesList = new ArrayList<>();
                if (replies != null) {
                    for (Comment reply : replies) {
                        Map<String, Object> repliesMap = new HashMap<>();
                        repliesMap.put("reply", reply);
                        User replier = userService.findUserById(reply.getUserId());
                        repliesMap.put("replier", replier);
                        User replyTo = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        repliesMap.put("target", replyTo);
                        repliesList.add(repliesMap);
                    }
                }
                map.put("replies", repliesList);
                map.put("replyCount", commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId()));
                comments.add(map);
            }
        }
        if (hostHolder.getUser()!= null) {
            int totalMessage = letterService.getTotalLetter(hostHolder.getUser().getId(), CONVERSATION_ID_TOTAL);
            model.addAttribute("totalMessage", totalMessage);
        }
        model.addAttribute("post", post);
        model.addAttribute("poster", poster);
        model.addAttribute("comments", comments);

        return "/site/discuss-detail";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "You haven't log in yet!");
        }

        DiscussPost post = new DiscussPost();
        post.setContent(content);
        post.setTitle(title);
        post.setCreateTime(new Date());
        post.setUserId(user.getId());
        discussPostService.addDiscussPost(post);

        return CommunityUtil.getJSONString(0, "Post Successful!");
    }

}
