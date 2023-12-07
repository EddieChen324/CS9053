package com.community.service;

import com.community.dao.UserMapper;
import com.community.entity.User;
import com.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SettingService {

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> changePassword(User user, String currentPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        String currentMD5 = CommunityUtil.md5(currentPassword + user.getSalt());
        String realMD5 = user.getPassword();
        if (!Objects.equals(currentMD5, realMD5)) {
            map.put("passwordMsg", "Current password is incorrect!");
        } else {
            if (newPassword == null) {
                map.put("newPasswordMsg", "new password can't be null!");
            }
            else {
                userMapper.updatePassword(user.getId(), CommunityUtil.md5(newPassword + user.getSalt()));
            }
        }
        return map;
    }
}
