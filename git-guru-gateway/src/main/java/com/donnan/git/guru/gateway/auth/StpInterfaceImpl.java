package com.donnan.git.guru.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

import com.donnan.git.guru.api.user.constant.UserPermission;
import com.donnan.git.guru.api.user.constant.UserRole;
import com.donnan.git.guru.api.user.constant.UserStateEnum;
import com.donnan.git.guru.api.user.response.data.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口
 *
 * @author Donnan
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);

        if (userInfo.getUserRole() == UserRole.ADMIN ) {
            return List.of(UserPermission.BASIC.name());
        }

        if (userInfo.getState().equals(UserStateEnum.INIT.name())) {
            return List.of(UserPermission.BASIC.name());
        }

        if (userInfo.getState().equals(UserStateEnum.FROZEN.name())) {
            return List.of(UserPermission.FROZEN.name());
        }

        return List.of(UserPermission.NONE.name());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
        if (userInfo.getUserRole() == UserRole.ADMIN) {
            return List.of(UserRole.ADMIN.name());
        }
        return List.of(UserRole.CUSTOMER.name());
    }
}
