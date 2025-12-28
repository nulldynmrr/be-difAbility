package com.ippl.difability.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.response.user.UserBaseResponse;
import com.ippl.difability.entity.User;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;
    
    public List<UserBaseResponse> getUsers(){
        return userRepository.findAll()
            .stream()
            .map(this::buildResponse)
            .toList(); 
    }

    public void deleteUser(String username, Long userId){
        User admin = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
        
        logService.log(
            username,
            admin.getRole().name(),
            "DELETE_USER",
            "Admin berhasil menghapus user dengan ID " + userId
        );
    }

    private UserBaseResponse buildResponse(User user){
        return new UserBaseResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isProfileCompleted(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}