package com.retrip.crew.infra.util;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

public abstract class PaginationUtils {

    public static Pageable createPageRequest(Pageable pageable, String order, String sort) {
        return PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sort), order)
        );
    }

    /** 무한스크롤 위해 hasNext 체크 및 제거하는 util */
    public static <T> Slice<T> checkEndPage(Pageable pageable, List<T> results) {
        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }
}
