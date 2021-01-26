package com.project.release.service;


import com.project.release.repositoriy.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;


    //앨범 생성,,,
    //사진 추가 .....




}
