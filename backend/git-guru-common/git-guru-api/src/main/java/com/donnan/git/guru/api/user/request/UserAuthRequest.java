package com.donnan.git.guru.api.user.request;

import com.donnan.git.guru.base.request.BaseRequest;
import lombok.*;

/**
 * @author Donnan
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest extends BaseRequest {

    private Long userId;
    private String realName;
    private String idCard;

}
