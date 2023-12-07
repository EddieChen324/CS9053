package com.community.Controller;

import com.community.annotation.LoginRequired;
import com.community.entity.LoginTicket;
import com.community.entity.User;
import com.community.service.LetterService;
import com.community.service.SettingService;
import com.community.service.UserService;
import com.community.util.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SettingService settingService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LetterService letterService;


    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @PostConstruct
    public void init() {
        FileUtil.createPath(uploadPath);
    }

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSetting(Model model) {
        if (hostHolder.getUser()!= null) {
            int totalMessage = letterService.getTotalLetter(hostHolder.getUser().getId(), CONVERSATION_ID_TOTAL);
            model.addAttribute("totalMessage", totalMessage);
        }
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.POST)
    public String changePassword(Model model, HttpServletRequest request, String currentPassword, String newPassword) {
        String ticket = CookieUtil.getValue(request, "ticket");
        User user = hostHolder.getUser();
        Map<String, Object> map = settingService.changePassword(user, currentPassword, newPassword);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "You successfully revised the password. It will automatically turn to the login page after 8 seconds.");
            model.addAttribute("target", "/login");
            userService.logout(ticket);
            return "/site/operate-result";
        }
        model.addAttribute("passwordMsg", map.get("passwordMsg"));
        model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/Upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImg, Model model) throws IOException {
        if (headerImg == null) {
            model.addAttribute("error", "You haven't select picture yet!");
            return "/site/setting";
        }

        String fileName = headerImg.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "Invalid file format");
            return "/site/setting";
        }

        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImg.transferTo(dest);
        } catch (IOException e) {
            logger.error("Upload file failed: " + e.getMessage());
            throw new RuntimeException("Upload file failed, The server isn't working", e);
        }

        //更新当前用户头像路径
        User user = hostHolder.getUser();
        //更新头像路径 需要显示时则会向该路径发送get请求
        String headUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //从服务器找到图像文件，并通过HttpServletResponse对象将其发送回客户端（浏览器

        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try {
            //获取响应的输出流 向客户端发送数据
            OutputStream os = response.getOutputStream();
            //创建一个指向要发送的文件的输入流
            FileInputStream fis = new FileInputStream(fileName);
            // 创建一个缓冲区，用于存储从文件中读取的数据
            byte[] buffer = new byte[1024];
            // 定义一个变量来存储每次读取的字节数
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        }   catch (IOException e){
            logger.error("Read headerUrl failed:" + e.getMessage());
        }
    }
}
