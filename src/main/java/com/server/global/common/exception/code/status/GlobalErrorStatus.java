package com.server.global.common.exception.code.status;

import com.server.global.common.exception.code.BaseCodeDto;
import com.server.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorStatus implements BaseCodeInterface {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),

	// === JWT 관련 에러 ===
	_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT401", "유효하지 않은 토큰입니다."),
	_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT402", "만료된 토큰입니다."),
	_EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "JWT404", "토큰이 없습니다."),
	_MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT405", "토큰 형식이 올바르지 않습니다."),
	_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "JWT406", "토큰 서명이 유효하지 않습니다."),
	_TOKEN_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT401-7", "토큰 검증에 실패했습니다."),

    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "권한이 없습니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),
    _REQUEST_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "COMMON406", "요청 본문 형식이 올바르지 않습니다. Enum 값이나 다른 데이터 형식을 확인해주세요."),
    _INTERNAL_PAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "페이지 에러, 0 이상의 페이지를 입력해주세요"),

	// S3 관련 에러
	_S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드에 실패했습니다."),
	_S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5002", "파일 삭제에 실패했습니다."),
	_FAILED_NOT_VALID_FORMAT(HttpStatus.BAD_REQUEST, "FILE001", "지원하지 않는 파일 확장자입니다."),
	_FAILED_READ_FILE(HttpStatus.BAD_REQUEST, "FILE002", "파일을 읽는 중 문제가 발생하였습니다."),

	//MCP 관련 에러
	_ALREADY_SAVED_MCP(HttpStatus.CONFLICT, "MCP409", "이미 저장된 MCP입니다."),

	// For test
	TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),
	;

	private final HttpStatus httpStatus;
	private final boolean isSuccess = false;
	private final String code;
	private final String message;

	@Override
	public BaseCodeDto getCode() {
		return BaseCodeDto.builder()
		                  .httpStatus(httpStatus)
		                  .isSuccess(isSuccess)
		                  .code(code)
		                  .message(message)
		                  .build();
	}
}
