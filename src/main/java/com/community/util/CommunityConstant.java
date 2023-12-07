package com.community.util;

public interface CommunityConstant {

    int ACTIVATION_SCUUESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAILURE = 2;

    //默认凭证超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    //记住状态下的凭证超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    //实体类型 帖子
    int ENTITY_TYPE_POST = 1;

    int ENTITY_TYPE_COMMENT = 2;

    String CONVERSATION_ID_LIKE = "like";

    String CONVERSATION_ID_COMMENT = "comment";

    String CONVERSATION_ID_FOLLOW = "follow";

    String CONVERSATION_ID_LETTER = "letter";

    String CONVERSATION_ID_NOTICE = "notice";

    String CONVERSATION_ID_TOTAL = "total";
}
