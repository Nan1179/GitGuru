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
public class UserActiveRequest extends BaseRequest {

    private Long userId;
    private String blockChainPlatform;
    private String blockChainUrl;

}
