package com.server.presentation.user;

import com.server.application.service.user.UserApplicationService;
import com.server.application.usecase.user.CreateRandomUserResult;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User APIs")
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping
    @Operation(summary = "랜덤 유저 생성")
    public BaseResponse<CreateRandomUserResponse> createAnonymousUser() {
        CreateRandomUserResult result = userApplicationService.createRandomUser();
        return BaseResponse.onSuccess(new CreateRandomUserResponse(result.userId(), result.nickname(), result.createdAt()));
    }
}
