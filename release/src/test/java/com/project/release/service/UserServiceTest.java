package com.project.release.service;

import com.project.release.domain.user.User;
import com.project.release.domain.user.UserRequestDTO;
import com.project.release.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test(expected = IllegalStateException.class)
    public void 중복회원가입_이름() throws Exception{
        User user1 = User.builder()
                .name("maira")
                .code(100L)
                .pic("pic1")
                .introduction("intro1")
                .build();

        User user2 = User.builder()
                .name("maira")
                .code(101L)
                .pic("pic2")
                .introduction("intro2")
                .build();

        userService.join(user1);
        userService.join(user2);

        fail("예외가 발생해야 함 ");
    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원가입_코드() throws Exception{
        User user1 = User.builder()
                .name("maira")
                .code(100L)
                .pic("pic1")
                .introduction("intro1")
                .build();

        User user2 = User.builder()
                .name("maira2")
                .code(100L)
                .pic("pic2")
                .introduction("intro2")
                .build();

        userService.join(user1);
        userService.join(user2);

        fail("예외가 발생해야 함 ");
    }

    /*
    @Test(expected = IllegalStateException.class)
    public void 회원수정_중복이름체크() throws Exception{
        User user1 = User.builder()
                .name("maira")
                .code(100L)
                .pic("pic1")
                .introduction("intro1")
                .build();

        User user2 = User.builder()
                .name("sera")
                .code(101L)
                .pic("pic2")
                .introduction("intro2")
                .build();

        userService.join(user1);
        userService.join(user2);

        User updateUser = User.builder()
                .name("sera")
                .code(100L)
                .pic("pic1")
                .introduction("intro1")
                .build();

        UserRequestDTO dto = new UserRequestDTO(updateUser);
        System.out.println("================= " + user1.getId());
        userService.updateUser(user1.getId(), dto);

        fail("예외가 발생해야 함 ");
    }

     */




}