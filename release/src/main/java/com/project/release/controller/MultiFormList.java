package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MultiFormList {
    private List<MultiForm> multiFormList;
    private Long userId;
    private String userName;
}
