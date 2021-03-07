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
public interface AlbumRepositoryInter extends JpaRepository<Album, Long> {

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

    /*
    @Query("select a, count(f.id) as fc from Album a "
            + "join fetch a.user user "
            + "left join a.favoriteList f "
            + "where a.modifiedDate > :dateTime "
            + "group by a.id "
            + "having concat(function('lpad', count(f.id), 10, '0'), function('lpad', a.id, 10, '0')) < concat(function('lpad', :favCount, 10, '0'), function('lpad', :albumId, 10, '0')) "
            + "order by fc desc, a.id desc")
    public List<Album> findByFavoriteNextPage(@Param("dateTime") LocalDateTime dateTime, @Param("favCount") Long favCount, @Param("albumId") Long albumId, Pageable page);


     */
    /*

select
        album0_.album_id as col_0_0_,
        count(favoriteli2_.favorite_id) as cnt,
        user1_.user_id as user_id1_8_1_,
        album0_.album_id as album_id1_0_0_,
        album0_.create_date as create_d2_0_0_,
        album0_.modify_date as modify_d3_0_0_,
        album0_.description as descript4_0_0_,
        album0_.thumbnail as thumbnai5_0_0_,
        album0_.title as title6_0_0_,
        album0_.user_id as user_id8_0_0_,
        album0_.version as version7_0_0_,
        user1_.code as code2_8_1_,
        user1_.introduction as introduc3_8_1_,
        user1_.name as name4_8_1_,
        user1_.pic as pic5_8_1_
    from
        album album0_
    inner join
        user user1_
            on album0_.user_id=user1_.user_id
    left outer join
        favorite favoriteli2_
            on album0_.album_id=favoriteli2_.album_id
    group by
        album0_.album_id
    order by
        album0_.album_id desc

     */

    // 팔로잉의 앨범 리스트 조회 last modified 기준



}
