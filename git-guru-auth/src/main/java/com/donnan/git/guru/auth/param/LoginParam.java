package com.donnan.git.guru.auth.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Donnan
 */
@Setter
@Getter
public class LoginParam extends RegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
