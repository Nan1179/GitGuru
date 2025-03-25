package com.donnan.git.guru.api.user.response.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 简单的用户信息，只返回部分字段，避免过多不该返回的信息被返回
 *
 * @author Donnan
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;
}
