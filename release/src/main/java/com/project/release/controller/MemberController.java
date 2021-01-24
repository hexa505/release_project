package com.project.release.controller;

import com.project.release.domain.SessionUser;
import com.project.release.domain.User;
import com.project.release.domain.UserDTO;
import com.project.release.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final HttpSession httpSession;
    private final UserService userService;
    private Long testCode = 65801603L;


    //vue 단에서 무슨 정보 가지고 있을지 고민 필요함...
    @GetMapping("/members")
    public SessionUser checkRegistered(Principal principal) {

        //User user = userService.findByCode(Long.parseLong(principal.getName()));
        User user = userService.findByCode(testCode);

        if(user != null) {
            httpSession.setAttribute("user", new SessionUser(user));
        }

        return (SessionUser)httpSession.getAttribute("user");
    }

    @PostMapping("/members")
    public SessionUser register(@RequestBody UserDTO dto, Principal principal) {
        log.info("post mapping!!!!");
        System.out.println(dto);

        User user = User.builder()
                //.code(Long.parseLong(principal.getName()))
                .code(testCode)
                .name(dto.getName())
                .pic(dto.getPic())
                .introduction(dto.getIntroduction())
                .build();

        userService.join(user);

        httpSession.setAttribute("user", new SessionUser(user));

        return (SessionUser)httpSession.getAttribute("user");

    }

    @PutMapping("/members/{userId}")
    public User updateUser(@RequestBody UserDTO dto, @PathVariable("userId") String userId) {
        SessionUser requestUser = (SessionUser)httpSession.getAttribute("user");
        if(requestUser.getName().equals(userId)) {
            userService.updateUser(requestUser.getId(), dto);
        }
        return userService.findByCode(requestUser.getCode());
    }


}
