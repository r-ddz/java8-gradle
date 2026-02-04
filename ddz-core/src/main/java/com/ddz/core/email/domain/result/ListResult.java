package com.ddz.core.email.domain.result;

import java.util.List;

/**
 * 列表结果对象（当不需要分页但需要列表时使用）
 * 用途：非分页列表查询
 */
public class ListResult<T> extends ApiResult<List<T>> {

    private Integer totalCount;  // 列表总条数
}
