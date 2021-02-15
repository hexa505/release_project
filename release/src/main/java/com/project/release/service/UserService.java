package com.project.release.service;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.project.release.domain.user.User;
import com.project.release.domain.user.UserRequestDTO;
import com.project.release.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    @Value("${resources.location}")
    private String resourcesLocation;

    // 회원가입
    @Transactional
    public Long join(User user) {
        validateDuplicateUserByCode(user.getCode());
        validateDuplicateUserByName(user.getName());
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserPic(Long id, MultipartFile pic) throws ImageProcessingException, MetadataException, IOException {
        User user = userRepository.findOne(id);
        String picPath = imageService.createProfileImg(id, pic);
        user.updatePic(picPath);
    }

    // 회원 정보 수정
    @Transactional
    public User updateUser(Long id, UserRequestDTO dto) throws ImageProcessingException, MetadataException, IOException {
        User user = userRepository.findOne(id);
        if(!user.getName().equals(dto.getName())) {
            validateDuplicateUserByName(dto.getName());
        }
        user.updateInfo(dto.getName(), dto.getIntroduction());

        if(user.getPic() != null) {
            String originPic = resourcesLocation + user.getPic();
            File file = new File(originPic);
            if(file.exists()) file.delete();
        }

        if(!dto.getProfileImg().isEmpty()) {
            String newPic = imageService.createProfileImg(id, dto.getProfileImg());
            user.updatePic(newPic);
        }
        else {
            user.updatePic(null);
        }

        return user;
    }

    private void validateDuplicateUserByCode(Long code) {
        User findUser = userRepository.findByCode(code);
        if(findUser != null) {
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }
    }

    private void validateDuplicateUserByName(String name) {
        User findUser = userRepository.findByName(name);
        if(findUser != null) {
            throw new IllegalStateException("이미 존재하는 username 입니다.");
        }
    }

    //회원 탈퇴


    public User findByCode(Long code) {
        return userRepository.findByCode(code);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public User findById(Long id) {return userRepository.findById(id);}


}
