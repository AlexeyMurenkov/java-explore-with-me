package ru.practicum.ewm.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FromIndexPageRequest extends PageRequest {

    private final int from;

    protected FromIndexPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    public static PageRequest of(int from, int size) {
        return of(from, size, Sort.unsorted());
    }

    public static PageRequest of(int from, int size, Sort sort) {
        return new FromIndexPageRequest(from, size, sort);
    }

    @Override
    public long getOffset() {
        return from;
    }
}
