package com.donnan.git.guru.api.notice.service;

import com.donnan.git.guru.api.notice.response.NoticeResponse;

/**
 * @author Donnan
 */
public interface NoticeFacadeService {
    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
