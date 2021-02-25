package com.project.release.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FollowListResult<T> {

    private List<T> data;
    private Long lastIndex;

}
