package com.donnan.git.guru.api.user.response.data;

import com.donnan.git.guru.api.user.constant.UserRole;
import com.donnan.git.guru.api.user.constant.UserStateEnum;
import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyPhone;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Donnan
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo extends BasicUserInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @SensitiveStrategyPhone
    private String telephone;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private String state;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 注册时间
     */
    private Date createTime;

}
