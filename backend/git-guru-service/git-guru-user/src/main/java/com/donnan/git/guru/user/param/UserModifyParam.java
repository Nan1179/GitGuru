package com.donnan.git.guru.user.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户修改参数
 * @author Donnan
 */
@Setter
@Getter
public class UserModifyParam {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

}
