package com.donnan.git.guru.api.user.service;

import com.donnan.git.guru.api.user.request.*;
import com.donnan.git.guru.api.user.response.*;
import com.donnan.git.guru.api.user.response.data.UserInfo;
import com.donnan.git.guru.base.response.PageResponse;

/**
 * @author Donnan
 */
public interface UserFacadeService {

    /**
     * 用户信息查询
     * @param userQueryRequest
     * @return
     */
    UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户信息修改
     * @param userModifyRequest
     * @return
     */
    UserOperatorResponse modify(UserModifyRequest userModifyRequest);
}
