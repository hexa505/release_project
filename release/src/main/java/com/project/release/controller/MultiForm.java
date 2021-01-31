package com.project.release.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@ToString
public class MultiForm {
 private MultipartFile profileImage;
 private PhotoForm photoForm;
}
