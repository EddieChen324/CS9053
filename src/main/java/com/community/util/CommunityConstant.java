package com.community.util;

public interface CommunityConstant {

    int ACTIVATION_SCUUESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAILURE = 2;

    //默认凭证超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    //记住状态下的凭证超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
