package com.project.release.repository.album;

import com.project.release.domain.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    public List<Album> findAlbumsByUser_Name(String name);

    public void deleteAlbumById(Long id);


//    select *, CONCAT(LPAD(a.album_ID,10,'0'), FORMATDATETIME(a.MODIFY_DATE ,'dMMyyyyHHmmss')) as cursor
//    from ALBUM a
//    where REGEXP_LIKE(a.title, 'des') or REGEXP_LIKE(a.DESCRIPTION , 'des')
//    GROUP BY a.ALBUM_ID
//    having  cursor > CONCAT(LPAD(0,10,'0'), LPAD(FORMATDATETIME('2021-03-07 18:05:42.808592' ,'dMMyyyyHHmmss'), 10, '1'))
//    ORDER by cursor
//    limit 5;


    @Query(nativeQuery = true, value = " select *, CONCAT(LPAD(a.album_ID,10,'0'), FORMATDATETIME(a.MODIFY_DATE ,'dMMyyyyHHmmss')) as cursor " +
            "    from ALBUM a" +
            "    where REGEXP_LIKE(a.title, :keyword)" +
            "    GROUP BY a.ALBUM_ID" +
            "    having  cursor > CONCAT(LPAD(0,10,'0'), LPAD(FORMATDATETIME('2021-03-07 18:05:42.808592' ,'dMMyyyyHHmmss'), 10, '1')) " +
            "    ORDER by cursor" +
            "    limit 5")
    public List<Album> findAlbumsByKeyword(@Param("keyword") String keyword);

}
