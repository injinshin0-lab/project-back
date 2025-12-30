package kr.co.kosmo.project_back.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통 예외 처리 클래스
 * - 모든 컨트롤러에서 발생하는 예외를 한 곳에서 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid가 붙은 DTO 검증 실패 시 처리
     * (NotBlank, Size, Email 등)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // 필드별 에러 메시지를 담을 Map
        Map<String, String> errors = new LinkedHashMap<>();

        // 어떤 필드에서 어떤 에러가 났는지 정리
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(
                error.getField(),          // DTO 필드명
                error.getDefaultMessage()  // message 속성 값
            );
        });

        // 400 Bad Request + 에러 목록 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
