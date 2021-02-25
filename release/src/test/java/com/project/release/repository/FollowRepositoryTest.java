package com.project.release.repository;

import com.project.release.domain.Follow;
import com.project.release.domain.user.User;
import com.project.release.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;


    @Test
    public void 팔로우리스트조회() {
        System.out.println("initDB 실행됨 ================");
        User user1 = User.builder()
                .code(1L)
                .name("user1")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .code(2L)
                .name("user2")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .code(3L)
                .name("user3")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .code(4L)
                .name("user4")
                .build();
        userRepository.save(user4);

        Follow follow1 = Follow.builder()
                .user(user1)
                .followedUser(user2)
                .build();
        followRepository.save(follow1);

        Follow follow2 = Follow.builder()
                .user(user1)
                .followedUser(user3)
                .build();
        followRepository.save(follow2);

        Follow follow3 = Follow.builder()
                .user(user1)
                .followedUser(user4)
                .build();
        followRepository.save(follow3);

        // List<Follow> followList = followRepository.findByUserOrderByIdDesc(user1.getId(), PageRequest.of(0, 2));
        // followList.stream().forEach(f -> System.out.println(f.getId() + " " + f.getUser().getName() + " " + f.getFollowedUser().getName()));

        List<Follow> followList = followRepository.findByUserAndIdLessThanOrderByIdDesc(1L, 3L, PageRequest.of(0, 2));
        followList.stream().forEach(f -> System.out.println(f.getId() + " " + f.getUser().getName() + " " + f.getFollowedUser().getName()));

    }

    @Test
    public void 팔로잉리스트조회() {
        User user1 = User.builder()
                .code(1L)
                .name("user1")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .code(2L)
                .name("user2")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .code(3L)
                .name("user3")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .code(4L)
                .name("user4")
                .build();
        userRepository.save(user4);

        Follow follow1 = Follow.builder()
                .user(user2)
                .followedUser(user1)
                .build();
        followRepository.save(follow1);

        Follow follow2 = Follow.builder()
                .user(user3)
                .followedUser(user1)
                .build();
        followRepository.save(follow2);

        Follow follow3 = Follow.builder()
                .user(user4)
                .followedUser(user1)
                .build();
        followRepository.save(follow3);

        // List<Follow> followList = followRepository.findByFollowedUserOrderByIdDesc(1L, PageRequest.of(0, 3));
        // followList.stream().forEach(f -> System.out.println(f.getId() + " " + f.getUser().getName() + " " + f.getFollowedUser().getName()));

        List<Follow> followList = followRepository.findByFollowedUserAndIdLessThanOrderByIdDesc(1L, 3L, PageRequest.of(0, 2));
        followList.stream().forEach(f -> System.out.println(f.getId() + " " + f.getUser().getName() + " " + f.getFollowedUser().getName()));
    }
}