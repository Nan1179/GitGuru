package com.donnan.git.guru.base.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求
 *
 * @author Donnan
 */
@Setter
@Getter
public class PageRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页结果数
     */
    private int pageSize;
}
