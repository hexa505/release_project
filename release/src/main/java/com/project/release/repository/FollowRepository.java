package com.project.release.repository;

import com.project.release.domain.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 나 -> 상대가 팔로우 상태인지 확인
    public List<Follow> findByUser_IdAndFollowedUser_Id(Long userId, Long followedUserId);

    @Query("select f from Follow f "
            + "join fetch f.user from_user "
            + "where f.followedUser.id = :followedId")
    public List<Follow> findByFollowedUser(@Param("followedId") Long followedId);

    // 유저의 팔로잉 목록
    @Query("select f from Follow f "
            + "join fetch f.user from_user "
            + "join fetch f.followedUser to_user "
            + "where from_user.id = :userId "
            + "order by f.id desc")
    public List<Follow> findByUserOrderByIdDesc(@Param("userId") Long userId, Pageable page);

    @Query("select f from Follow f "
            + "join fetch f.user from_user "
            + "join fetch f.followedUser to_user "
            + "where from_user.id = :userId and "
            + "f.id < :followId "
            + "order by f.id desc")
    public List<Follow> findByUserAndIdLessThanOrderByIdDesc(@Param("userId") Long userId, @Param("followId") Long followId, Pageable page);

    // 유저를 팔로우하는 목록
    @Query("select f from Follow f "
            + "join fetch f.user from_user "
            + "join fetch f.followedUser to_user "
            + "where to_user.id = :followedUserId "
            + "order by f.id desc")
    public List<Follow> findByFollowedUserOrderByIdDesc(@Param("followedUserId") Long followedUserId, Pageable page);

    @Query("select f from Follow f "
            + "join fetch f.user from_user "
            + "join fetch f.followedUser to_user "
            + "where to_user.id = :followedUserId and "
            + "f.id < :followId "
            + "order by f.id desc")
    public List<Follow> findByFollowedUserAndIdLessThanOrderByIdDesc(@Param("followedUserId") Long followedUserId, @Param("followId") Long followId, Pageable page);

    // 유저의 팔로잉 목록 count
    public long countByUser_Id(Long userId);

    // 유저를 팔로우하는 목록 count
    public long countByFollowedUser_Id(Long followedUserId);

}
