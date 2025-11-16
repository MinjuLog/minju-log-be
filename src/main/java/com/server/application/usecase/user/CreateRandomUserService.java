package com.server.application.usecase.user;

import com.server.domain.entity.user.User;
import com.server.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRandomUserService implements CreateRandomUserUseCase {

    private final UserRepository userRepository;

    @Override
    public CreateRandomUserResult execute() {
        String randomNickname = "user-" + UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .nickname(randomNickname)
                .build();

        User saved = userRepository.save(user);

        return new CreateRandomUserResult(saved.getId(), saved.getNickname(), saved.getCreatedAt());
    }
}
