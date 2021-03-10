package com.project.release.service;

import com.project.release.domain.AlbumListDTO;
import com.project.release.domain.AlbumListResult;
import com.project.release.domain.album.Album;
import com.project.release.repository.album.AlbumRepository;
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
public class HomeAlbumService {

    private final AlbumRepository albumRepository;

    @Value("${resources.uri_path}")
    private String resourcesUriPath;

    // 최근 일주일 앨범 좋아요순 조회
    public AlbumListResult<AlbumListDTO, Integer> getPopularAlbums(LocalDateTime dateTime, Integer favCount, Long albumId, Pageable page) {
        List<Album> albums;
        if (favCount == null) {
            albums = albumRepository.findByFavoriteFirstPage(dateTime, page);
        } else {
            albums = albumRepository.findByFavoriteNextPage(dateTime, favCount, albumId, page);
        }

        Integer lastCount = albums.isEmpty() ? null : albums.get(albums.size() - 1).getFavoriteList().size();
        Long lastId = albums.isEmpty() ? null : albums.get(albums.size() - 1).getId();

        List<AlbumListDTO> albumList = albums.stream()
                .map(a -> new AlbumListDTO(a, a.getUser(), resourcesUriPath))
                .collect(Collectors.toList());

        return new AlbumListResult<>(albumList, lastId, lastCount);
    }


    public AlbumListResult<AlbumListDTO, Long> getAlbumsByKeyword(Long cursorId, String keyword) {

        if (cursorId == null || cursorId == 0) cursorId = Long.MAX_VALUE;
        List<Album> albums = albumRepository.findAlbumsByKeyword(cursorId.toString(), keyword);

        List<AlbumListDTO> albumList = albums.stream()
                .map(a -> new AlbumListDTO(a, a.getUser(), resourcesUriPath))
                .collect(Collectors.toList());

        String lastCursor = albums.isEmpty() ? "0" : albumRepository.getCursor(albums.get(albums.size() - 1).getId());

        return new AlbumListResult<>(albumList, cursorId, Long.parseLong(lastCursor));
    }



}
