package com.project.release.service;

import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import com.project.release.repository.FeedRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FeedServiceTest {

    @Autowired FeedService feedService;
    @Autowired FeedRepository feedRepository;
    @Autowired FollowService followService;
    @Autowired
    AlbumRepositoryInter albumRepositoryInter;
    @Autowired UserService userService;

    // 유저가 팔로잉하면 해당 유저의 앨범들을 자기 피드에 넣어야함
    @Test
    public void 팔로우_피드추가() {

        followService.follow(userService.findById(3L), userService.findById(2L));
        //feedRepository.findAll().stream().forEach(f -> System.out.println(f.getUser() + " " + f.getWriter() + " " + f.getAlbum().getId()));
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(10);

    }

    // 유저가 언팔로우하면 해당 유저의 앨범들을 자기 피드에서 삭제해야함
    @Test
    public void 언팔로우_피드삭제() {

        User follower = userService.findById(3L);
        User followee = userService.findById(2L);

        followService.follow(follower, followee);
        followService.unfollow(follower, followee);
        Assertions.assertThat(feedRepository.findAll().isEmpty()).isEqualTo(true);
    }

    // 유저가 앨범을 작성하면 해당 앨범을 팔로워의 피드에 넣어야함
    @Test
    public void 앨범작성_피드추가() {

        User user = userService.findById(1L);
        Album album = Album.builder()
                .user(user)
                .title("my account album")
                .build();
        albumRepositoryInter.save(album);
        feedService.addFeedOnAlbumCreated(user, album);

        //feedRepository.findAll().stream().forEach(f -> System.out.println(f.getUser().getName() + " " + f.getWriter().getName() + " " + f.getAlbum().getId()));
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(9);

    }

    // 유저가 앨범을 삭제하면 해당 앨범을 팔로워들의 피드에서 삭제해야함
    @Test
    public void 앨범삭제_피드삭제() {

        User user = userService.findById(1L);
        Album album = Album.builder()
                .user(user)
                .title("my account album")
                .build();
        albumRepositoryInter.save(album);
        feedService.addFeedOnAlbumCreated(user, album);
        feedService.deleteFeedOnAlbumDeleted(album.getId());

        Assertions.assertThat(feedRepository.findAll().isEmpty()).isEqualTo(true);

    }


}