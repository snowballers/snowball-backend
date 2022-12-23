package com.snowballer.api.service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;
import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.security.SecurityUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원탈퇴
     */
    @Transactional
    public void withdrawal() {
        User user = getCurrentUser()
            .orElseThrow(() -> new RestApiException(ErrorCode.REQUIRED_LOGIN));

        user.changeStateOff();
        userRepository.save(user);
    }

    /**
     * 권한없을 시 에러
     * @param user
     */
    public void checkAuthorized(User user) {
        if (!checkSelfConfirmation(user)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * 현재 로그인된 유저가 매개변수 user와 같은지 확인
     * @param user
     * @return
     */
    public boolean checkSelfConfirmation(User user) {
        if (getCurrentUser().get().equals(user)) {
            return true;
        }
        return false;
    }

    /**
     * 현재 로그인된 유저 반환
     * @return
     */
    public Optional<User> getCurrentUser() {
        return SecurityUtil.getCurrentUsername()
          .flatMap(id -> userRepository.findByIdAndState(Long.valueOf(id), UserState.ACTIVE));
    }
}
