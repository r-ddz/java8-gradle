package com.ddz.core.email.domain.result;

import java.util.List;

/**
 * 分页结果对象
 * 用途：分页查询响应
 */
public class PageResult<T> extends ApiResult<List<T>> {

    private Integer pageNum;      // 当前页码
    private Integer pageSize;     // 每页大小
    private Long total;           // 总记录数
    private Integer totalPages;   // 总页数
    private Boolean hasPrevious;  // 是否有上一页
    private Boolean hasNext;      // 是否有下一页


}
