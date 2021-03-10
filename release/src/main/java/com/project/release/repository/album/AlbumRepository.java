package com.project.release.repository.album;

import com.project.release.domain.album.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    public List<Album> findByUser_Id(Long userId);

    // 유저의 앨범 리스트 조회 last modified 기준
    @Query("select a from Album a "
            + "join fetch a.user user "
            + "where user.id = :userId "
            + "order by a.modifiedDate desc")
    public List<Album> findByUserIdFirstPage(@Param("userId") Long userId, Pageable page);

    @Query("select a from Album a "
            + "join fetch a.user user "
            + "where user.id = :userId and "
            + "a.modifiedDate < :dateTime "
            + "order by a.modifiedDate desc")
    public List<Album> findByUserIdNextPage(@Param("userId") Long userId, @Param("dateTime") LocalDateTime dateTime, Pageable page);

    // favorite 기준 앨범 리스트 조회
    @Query("select a, count(f.id) as fc from Album a "
            + "join fetch a.user user "
            + "left join a.favoriteList f "
            + "where a.modifiedDate > :dateTime "
            + "group by a.id "
            + "order by fc desc, a.id desc")
    public List<Album> findByFavoriteFirstPage(@Param("dateTime") LocalDateTime dateTime, Pageable page);

    @Query("select a from Album a "
            + "join fetch a.user user "
            + "left join a.favoriteList f "
            + "where a.modifiedDate > :dateTime and "
            + "(f.size < :favCount or "
            + "f.size = :favCount and a.id < :albumId) "
            + "order by f.size desc, a.id desc")
    public List<Album> findByFavoriteNextPage(@Param("dateTime") LocalDateTime dateTime, @Param("favCount") Integer favCount, @Param("albumId") Long albumId, Pageable page);

    public List<Album> findAlbumsByUser_Name(String name);

    public void deleteAlbumById(Long id);


//    select *, CONCAT(LPAD(a.album_ID,10,'0'), FORMATDATETIME(a.MODIFY_DATE ,'YYMMDDHHMMSS')) as cursor
//    from ALBUM a
//    where REGEXP_LIKE(a.title, 'des') or REGEXP_LIKE(a.DESCRIPTION , 'des')
//    GROUP BY a.ALBUM_ID
//    having  cursor > CONCAT(LPAD(0,10,'0'), LPAD(FORMATDATETIME('2021-03-07 18:05:42.808592' ,'dMMyyyyHHmmss'), 10, '1'))
//    ORDER by cursor
//    limit 5;
//
//    select * from ALBUM  ;

    @Query(nativeQuery = true, value =
            "    select *, CONCAT( FORMATDATETIME(a.MODIFY_DATE ,'YYMMDDHH'), LPAD(a.album_ID,10,'0')) as cursor" +
            "    from ALBUM a " +
                    "inner join User u on a.User_id = u.USER_id" +
            "    where REGEXP_LIKE(a.title, :keyword) or REGEXP_LIKE(a.DESCRIPTION , :keyword)" +
            "    or a.ALBUM_ID = (select distinct at.ALBUM_ID from ALBUM_TAG at, TAG t WHERE REGEXP_LIKE(t.tag_NAME, :keyword))" +
            "    GROUP BY a.ALBUM_ID" +
            "    having cursor < :lastId" +
            "    ORDER by cursor DESC" +
            "    limit 10")
    public List<Album> findAlbumsByKeyword(@Param("lastId") String lastId, @Param("keyword") String keyword);

    @Query(nativeQuery = true, value =
            "    select CONCAT(FORMATDATETIME(a.MODIFY_DATE ,'YYMMDDHH'), LPAD(a.album_ID,10,'0')) as cursor" +
                    "    from ALBUM a" +
                    "    where a.ALBUM_ID = :id" +
                    "    limit 1")
    public String getCursor(@Param("id") Long id);


}
