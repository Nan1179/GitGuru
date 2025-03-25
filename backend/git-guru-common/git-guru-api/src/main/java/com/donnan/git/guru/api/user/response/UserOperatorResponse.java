package com.donnan.git.guru.api.user.response;


import com.donnan.git.guru.api.user.response.data.UserInfo;
import com.donnan.git.guru.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @author Donnan
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
