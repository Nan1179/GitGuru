package com.donnan.git.guru.user.facade;

import com.donnan.git.guru.api.user.request.*;
import com.donnan.git.guru.api.user.request.condition.UserIdQueryCondition;
import com.donnan.git.guru.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.donnan.git.guru.api.user.request.condition.UserPhoneQueryCondition;
import com.donnan.git.guru.api.user.response.UserOperatorResponse;
import com.donnan.git.guru.api.user.response.UserQueryResponse;
import com.donnan.git.guru.api.user.response.data.UserInfo;
import com.donnan.git.guru.api.user.service.UserFacadeService;
import com.donnan.git.guru.base.response.PageResponse;
import com.donnan.git.guru.rpc.facade.Facade;
import com.donnan.git.guru.user.domain.entity.User;
import com.donnan.git.guru.user.domain.entity.convertor.UserConvertor;
import com.donnan.git.guru.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Donnan
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Facade
    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {
        //使用switch表达式精简代码，如果这里编译不过，参考我的文档调整IDEA的JDK版本
        //文档地址：https://thoughts.aliyun.com/workspaces/6655879cf459b7001ba42f1b/docs/6673f26c5e11940001c810fb#667971268a5c151234adcf92
        User user = switch (userQueryRequest.getUserQueryCondition()) {
            case UserIdQueryCondition userIdQueryCondition:
                yield userService.findById(userIdQueryCondition.getUserId());
            case UserPhoneQueryCondition userPhoneQueryCondition:
                yield userService.findByTelephone(userPhoneQueryCondition.getTelephone());
            case UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition:
                yield userService.findByTelephoneAndPass(userPhoneAndPasswordQueryCondition.getTelephone(), userPhoneAndPasswordQueryCondition.getPassword());
            default:
                throw new UnsupportedOperationException(userQueryRequest.getUserQueryCondition() + "'' is not supported");
        };

        UserQueryResponse<UserInfo> response = new UserQueryResponse();
        response.setSuccess(true);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        response.setData(userInfo);
        return response;
    }

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.getTelephone());
    }

    @Override
    @Facade
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        return userService.modify(userModifyRequest);
    }
}
