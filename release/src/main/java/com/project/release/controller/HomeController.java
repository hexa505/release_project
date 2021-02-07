package com.project.release.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 메인 컨트롤러
 * templates/index.html 로 이동
 */

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }
}
