package com.project.release.service;


import com.project.release.controller.AlbumForm;
import com.project.release.controller.MultiForm;
import com.project.release.domain.album.Album;
import com.project.release.repositoriy.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumTagService albumTagService;

    @Transactional
    public Long createAlbum(MultiForm form, String userName) {

        //userName으로 유저 엔티티 찾아서 유저 인스턴스 넣는 걸로 바꿀 것
        AlbumForm albumForm = form.getAlbumForm();
        Album album = Album.builder().userName(userName)
                .thumbnail(albumForm.getPhoto().getOriginalFilename())
                .title(albumForm.getTitle())
                .description(albumForm.getDescription()).build();
        albumRepository.save(album);
        albumTagService.saveTags(album, stringToTagSet(albumForm.getTagString()));
        return album.getAlbumId();

    }

    // tags 스트링을 쪼개 주기..
            /*    해시태그 작성 방식..
            #태그, #태그, ...
            #이런 식으로 띄어쓰기는 허용하지 않음
            #해시_태그 이건 괜찮*/
    public Set<String> stringToTagSet(String tags) {
        //1. , " " 로 쪼개기
        //2. #있는 것만 #떼고 TagSet에 저장
        StringTokenizer tk = new StringTokenizer(tags, ", ");
//        Set<String> hashtags = new Set<String>();
        HashSet<String> hashtags = new HashSet<>();
        while (tk.hasMoreTokens()) {
            String token = tk.nextToken();
            System.out.println("token = " + token);
            if (token.charAt(0) == '#') {
                System.out.println(token.substring(1));
                hashtags.add(token.substring(1));
                System.out.println("hashtags.toString() = " + hashtags.toString());
            }
        }

        return hashtags;
    }


    //유저name으로 앨범 리스트 조회
    public List<Album> findAlbumsByUserName(String userName) {
        return albumRepository.findByUserName(userName);
    }

    //앨범이름으로 앨범 조회,,,,
    public List<Album> findAlbumsByAlbumTitle(String title) {
        return albumRepository.findByAlbumTitle(title);
    }

    public Album findOneById(Long id) {
        return albumRepository.findOne(id);
    }
}
