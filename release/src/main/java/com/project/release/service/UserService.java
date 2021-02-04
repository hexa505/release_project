package com.project.release.service;

import com.project.release.domain.user.User;
import com.project.release.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public Long join(User user) {
        validateDuplicateUserByCode(user.getCode());
        validateDuplicateUserByName(user.getName());
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserPic(Long id, String pic) {
        User user = userRepository.findOne(id);
        user.updatePic(pic);
    }

    // 회원 정보 수정
    /*
    @Transactional
    public User updateUser(Long id, UserRequestDTO dto) {
        validateDuplicateUserByName(dto.getUserInfo().getName());
        User user = userRepository.findOne(id);
        user.updateInfo(dto.getUserInfo().getName(), dto.getProfileImg(), dto.getUserInfo().getIntroduction());
        return user;
    }

     */

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


}
