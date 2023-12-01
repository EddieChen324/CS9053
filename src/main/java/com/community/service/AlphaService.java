package com.community.service;

import com.community.dao.AlphaDao;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化 AlphaService");
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化 AlphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁 AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}
