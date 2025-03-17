package com.donnan.git.guru.api.user.request;

import com.donnan.git.guru.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Donnan
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest extends BaseRequest {
    @NotNull(message = "userId不能为空")
    private Long userId;
    private String nickName;
    private String password;
    private String profilePhotoUrl;
    private String telephone;

}
