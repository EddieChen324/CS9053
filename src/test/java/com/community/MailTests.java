package com.community;

import com.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes =  CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient client;

    @Autowired
    private TemplateEngine engine;

    @Test
    public void testTextMail() {
        client.sendMail("longc0324@foxmail.com", "TEST", "Welcome");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = engine.process("/mail/demo", context);
        System.out.println(content);

        client.sendMail("longc0324@foxmail.com", "HTML", content);
    }

}
