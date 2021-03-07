package com.project.release.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AlbumListResult<T1, T2> {

    private List<T1> data;
    private Long lastId;
    private T2 optionalId;

}
