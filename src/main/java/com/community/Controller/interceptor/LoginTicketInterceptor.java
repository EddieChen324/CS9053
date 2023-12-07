package com.community.Controller.interceptor;

import com.community.entity.LoginTicket;
import com.community.service.UserService;
import com.community.util.CookieUtil;
import com.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.community.entity.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

//每调用一个controller都要走一遍这个流程
//检查用户的登录状态，并在请求处理过程中保持用户信息，以及在请求结束后进行清理。
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //在调用相应的controller前会先调用preHandle
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Request中取Cookie得到ticket
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效   status有效且没过期
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    //调用了指定的controller后 在视图前处理
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //把用户清理掉
        hostHolder.clear();
    }
}
