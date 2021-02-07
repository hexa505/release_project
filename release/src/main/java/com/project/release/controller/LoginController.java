package com.project.release.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        System.out.println("===============login page!!");
        return "login";
    }

    @RequestMapping("/callback")
    public @ResponseBody String callback(String code) {
        return "callback";
    }


}
