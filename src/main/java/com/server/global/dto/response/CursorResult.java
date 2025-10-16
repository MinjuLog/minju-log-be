package com.server.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorResult<T> {

    private T data;
    private CursorPagination pagination;
}
