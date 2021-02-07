package com.project.release.controller;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.project.release.domain.user.*;
import com.project.release.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final HttpSession httpSession;
    private final UserService userService;
    //private final Long testCode = 65801603L;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    /*
    로그인
     */
    @GetMapping("/members")
    public String checkRegistered(HttpServletResponse response, Principal principal) {
        User user = userService.findByCode(Long.parseLong(principal.getName()));
        //User user = userService.findByCode(testCode);

        if(user == null) {
            return null;
        }

        httpSession.setAttribute("user", new SessionUser(user));

        return user.getName();
    }

    /*
    회원가입
     */
    @PostMapping("/members")
    public String register(@ModelAttribute @Valid UserRequestDTO dto, Principal principal) throws IOException, ImageProcessingException, MetadataException {

        User user = User.builder()
                .code(Long.parseLong(principal.getName()))
                //.code(testCode)
                .name(dto.getName())
                .introduction(dto.getIntroduction())
                .build();

        Long id = userService.join(user);
        if(!dto.getProfileImg().isEmpty()) {
            userService.updateUserPic(id, dto.getProfileImg());
        }

        return "user created";

    }

    /*
    회원 조회
     */
    @GetMapping("/members/{username}")
    public UserResponseDTO showUser(@PathVariable("username") String username) {
        User user = userService.findByName(username);
        UserResponseDTO response = new UserResponseDTO(user, resourcesUriPath);
        return response;
    }

    /*
    회원정보 수정
     */
    @PostMapping("/members/{username}")
    public void updateUser(@ModelAttribute @Valid UserRequestDTO dto, @PathVariable("username") String username) throws IOException, MetadataException, ImageProcessingException {
        SessionUser requestUser = (SessionUser)httpSession.getAttribute("user");
        if(requestUser.getName().equals(username)) {
            User user = userService.updateUser(requestUser.getId(), dto);
            httpSession.setAttribute("user", new SessionUser(user));
        }

        return;
    }

    // 회원 탈


}
