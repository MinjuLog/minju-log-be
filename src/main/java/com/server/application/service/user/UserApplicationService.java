package com.server.application.service.user;

import com.server.application.usecase.user.CreateRandomUserResult;
import com.server.application.usecase.user.CreateRandomUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService {

    private final CreateRandomUserUseCase createRandomUserUseCase;

    public CreateRandomUserResult createRandomUser() {
        return createRandomUserUseCase.execute();
    }
}
