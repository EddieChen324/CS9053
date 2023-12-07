package com.community.Controller;

import com.community.entity.User;
import com.community.service.LetterService;
import com.community.service.UserService;
import com.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private LetterService letterService;

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String getProfile(@RequestParam("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user != null) {
            int totalMessage = letterService.getTotalLetter(user.getId(), CONVERSATION_ID_TOTAL);
            model.addAttribute("totalMessage", totalMessage);
        }
        model.addAttribute("user", user);
        return "site/profile";
    }

}
