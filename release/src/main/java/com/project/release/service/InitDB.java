package com.project.release.service;

import com.project.release.domain.Bookmark;
import com.project.release.domain.Follow;
import com.project.release.domain.album.Album;
import com.project.release.domain.album.AlbumTag;
import com.project.release.domain.album.Tag;
import com.project.release.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * DB initialization
 */
@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.userInit();
        initService.followInit();
        initService.albumInit();
        initService.albumTagInit();
        initService.bookmarkInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final UserService userService;
        private final AlbumService albumService;

        public void userInit() {
            User user = User.builder()
                    .name("myaccount")
                    .code(65801603L)
                    .build();
            em.persist(user);

            for(int i = 2; i <= 10; i++) {
                User newUser = User.builder()
                        .name("user" + Integer.toString(i))
                        .code((long)i)
                        .build();
                em.persist(newUser);
            }

        }

        public void followInit() {
            User user = userService.findByName("myaccount");

            for(int i = 2; i <= 10; i++) {
                User findUser = userService.findByName("user" + Integer.toString(i));

                // myaccount -> users
                Follow follow1 = Follow.builder()
                        .user(user)
                        .followedUser(findUser)
                        .build();
                em.persist(follow1);

                // users -> myaccount
                Follow follow2 = Follow.builder()
                        .user(findUser)
                        .followedUser(user)
                        .build();
                em.persist(follow2);
            }
        }

        public void albumInit() {
            User user2 = userService.findByName("user2");

            for(int i = 1; i <= 10; i++) {
                Album album = Album.builder()
                        .user(user2)
                        .title("album title " + Integer.toString(i))
                        .build();

                em.persist(album);
            }

        }

        public void albumTagInit() {
            Album album = albumService.findOneById((long)1);

            for(int i = 1; i <= 4; i++) {
                Tag tag = Tag.builder().tagName("tag" + Integer.toString(i)).build();
                em.persist(tag);

                AlbumTag at = new AlbumTag();
                at.setAlbum(album);
                at.setTag(tag);
                em.persist(at);
            }

        }

        public void bookmarkInit() {
            User user = userService.findByName("myaccount");

            for(int i = 1; i <=9; i++) {
                Album album = albumService.findOneById((long)i);

                Bookmark bookmark = Bookmark.builder()
                        .album(album)
                        .user(user)
                        .build();

                em.persist(bookmark);
            }

        }

    }

}


