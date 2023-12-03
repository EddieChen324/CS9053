package com.community;

import com.community.dao.DiscussPostMapper;
import com.community.dao.LoginTicketMapper;
import com.community.dao.UserMapper;
import com.community.entity.DiscussPost;
import com.community.entity.LoginTicket;
import com.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes =  CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("Liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("Sam");
        user.setPassword("12345");
        user.setSalt("abc");
        user.setEmail("lc5112@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "324219");
        System.out.println(rows);
    }

    @Test
    public void selectPost() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }

        int count = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(count);
    }

    @Test
    public void insertTicket() {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(101);
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        ticket.setTicket("abc");

        loginTicketMapper.insertLoginTicket(ticket);
    }

    @Test
    public void selectTicket() {
        LoginTicket ticket = loginTicketMapper.selectByTicket("abc");
        System.out.println(ticket);
    }
}
