package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class MultiForm {
    private List<PhotoForm> photoFormList;
    private AlbumForm albumForm;
}
