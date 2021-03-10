package com.project.release.service;

import com.project.release.domain.Bookmark;
import com.project.release.domain.BookmarkDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.album.Album;
import com.project.release.domain.user.User;
import com.project.release.repository.BookmarkRepository;
import com.project.release.repository.UserRepository;
import com.project.release.repository.album.AlbumRepository;
import com.project.release.repository.album.AlbumTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    // 북마크 여부 조회
    public boolean checkBookmark(Long userId, Long albumId) {
        return !bookmarkRepository.findByAlbum_IdAndUser_Id(albumId, userId).isEmpty();
    }

    // 북마크 추가 not null 추가해야하나?
    @Transactional
    public void addBookmark(Long userId, Long albumId) {
        if(checkBookmark(userId, albumId)) return;

        User user = userRepository.findById(userId);
        Album album = albumRepository.findById(albumId).stream().findFirst().orElse(null);
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .album(album)
                .build();

        bookmarkRepository.save(bookmark);
    }

    // 북마크 취소
    @Transactional
    public void deleteBookmark(Long userId, Long albumId) {
        Bookmark bookmark = bookmarkRepository.findByAlbum_IdAndUser_Id(albumId, userId).stream()
                .findFirst().orElse(null);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        }
    }

    // 북마크 리스트 조회
    public AlbumListResult<BookmarkDTO, LocalDateTime> getBookmarkList(Long userId, Long cursorId, LocalDateTime cursorDateTime, Pageable page) {
        List<Bookmark> findBookmarks;
        if(cursorId == null || cursorDateTime == null) { // 첫 스크롤
            findBookmarks = bookmarkRepository.findBookmarkFirstPage(userId, page);
        }
        else {
            findBookmarks = bookmarkRepository.findBookmarkNextPage(userId, cursorId, cursorDateTime, page);
        }

        Long lastId = findBookmarks.isEmpty() ? null : findBookmarks.get(findBookmarks.size() -1 ).getId();
        LocalDateTime lastDateTime = findBookmarks.isEmpty() ? null : findBookmarks.get(findBookmarks.size() - 1).getAlbum().getModifiedDate();

        List<BookmarkDTO> bookmarkList = findBookmarks.stream()
                .map(b -> new BookmarkDTO(b, resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult<>(bookmarkList, lastId, lastDateTime);

    }

    // 북마크 열람 시 앨범 버전 갱신
    @Transactional
    public void syncBookmarkVersion(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).stream().findFirst().orElse(null);
        bookmark.updateVersion(bookmark.getAlbum().getVersion());
    }

    // 앨범의 북마크 수 조회
    public Long countBookmarkOfAlbum(Long albumId) {
        return bookmarkRepository.countByAlbum_Id(albumId);
    }

    // 사용자가 한 북마크 수 조회
    public Long countBookmarks(Long userId) {
        return bookmarkRepository.countByUser_Id(userId);
    }

}
