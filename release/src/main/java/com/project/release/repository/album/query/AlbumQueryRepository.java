package com.project.release.repository.album.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AlbumQueryRepository {
    private final EntityManager em;


    public List<AlbumQueryDTO> findByUserName(String userName) {
        return em.createQuery(
                "select new com.project.release.repositoriy.album.query.AlbumQueryDTO(a.albumId, a.userId, a.thumbnail, a.description, a.title, a.userName) " +
                        " from Album a" +
                        " where a.userName = :userName", AlbumQueryDTO.class
        ).setParameter("userName", userName).getResultList();
    }


    //포토리스트까지 쿼리 조회
    public List<AlbumQueryDTO> findByUserNameQuery(String userName) {
        List<AlbumQueryDTO> albums = findByUserName(userName);
        List<Long> albumIds = albums.stream().map(album -> album.getAlbumId()).collect(Collectors.toList());
        Map<Long, List<TagDTO>> tagMap = findTagmap(albumIds);
        albums.forEach(album -> album.setAlbumTags(tagMap.get(album.getAlbumId())));
        Map<Long, List<PhotoDTO>> photoListMap = findPhotoMap(albumIds);
        albums.forEach(album -> album.setPhotoList(photoListMap.get(album.getAlbumId())));

        return albums;
    }

    private Map<Long, List<PhotoDTO>> findPhotoMap(List<Long> albumIds) {

        List<PhotoDTO> photos = em.createQuery(
                "select new com.project.release.repositoriy.album.query.PhotoDTO(p.photoId, a.albumId, p.num, p.pic, p.title, p.description)" +
                        " from Album a " +
                        " join a.photoList p" +
                        " where a.albumId in :albumIds", PhotoDTO.class
        ).setParameter("albumIds", albumIds).getResultList();

        Map<Long, List<PhotoDTO>> photoDTOMap = photos.stream().collect(Collectors.groupingBy(PhotoDTO::getAlbumId));
        return photoDTOMap;
    }


    public Map<Long, List<TagDTO>> findTagmap(List<Long> albumIds){
        List<TagDTO> tags = em.createQuery(
                "select new com.project.release.repositoriy.album.query.TagDTO(at.album.albumId, t.tagName) from AlbumTag at" +
                        " join at.tag t " +
                        " where at.album.albumId in :albumIds", TagDTO.class
        ).setParameter("albumIds", albumIds).getResultList();

        Map<Long, List<TagDTO>> tagDTOMap = tags.stream().collect(Collectors.groupingBy(TagDTO::getAlbumId));
        return tagDTOMap;
    }

}
