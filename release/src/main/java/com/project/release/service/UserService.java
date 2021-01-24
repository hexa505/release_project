package com.project.release.service;

import com.project.release.domain.User;
import com.project.release.domain.UserDTO;
import com.project.release.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        //name 중복체크 필요함
        return user.getId();
    }

    //update
    @Transactional
    public void updateUser(Long id, UserDTO dto) {
        //User user = userRepository.findById(id);
        User user = userRepository.findOne(id);
        user.updateInfo(dto.getName(), dto.getPic(), dto.getIntroduction());
        //name 중복체크 필요함
    }

    public User findByCode(Long code) {
        return userRepository.findByCode(code);
    }


}
