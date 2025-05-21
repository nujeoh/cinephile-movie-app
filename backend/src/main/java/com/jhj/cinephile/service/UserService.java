package com.jhj.cinephile.service;

import com.jhj.cinephile.dto.UserLoginRequest;
import com.jhj.cinephile.dto.UserSignupRequest;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.exception.AuthenticationException;
import com.jhj.cinephile.exception.EmailAlreadyExistsException;
import com.jhj.cinephile.exception.UserNotFoundException;
import com.jhj.cinephile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    // ユーザー取得
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    // 会員登録
    public void register(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("このメールアドレスは既に使用されています。");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();
        userRepository.save(user);
    }

    // ログイン認証
    public UserEntity authenticateUser(UserLoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("認証に失敗しました。"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("認証に失敗しました。");
        }
        return user;
    }

    // プロフィール画像更新
    @Transactional
    public String updateProfileImage(UserEntity user, MultipartFile file) throws IOException {
        UserEntity currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("ユーザーが見つかりません。"));
        String oldKey = currentUser.getProfileImageKey();
        String newUrl = fileStorageService.uploadAndReplace(file, oldKey);
        String newKey = fileStorageService.extractKeyFromUrl(newUrl);
        currentUser.updateProfileImage(newUrl, newKey);
        return newUrl;
    }
}