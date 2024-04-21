package msa.reservation.exception;


import lombok.extern.slf4j.Slf4j;
import msa.reservation.dto.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
// Controller에서 오류 발생시 Handler
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errorList = ex
                // MethodArgumentNotValidException에서 발생한 유효성 검사 에러들을 가져옴. 각각의 에러는 필드와 관련된 정보를 가지고 있다.
                .getBindingResult()
                .getFieldErrors()
                .stream()
                // DefaultMessageSourceResolvable의 getDefaultMessage 메서드를 적용하여 해당 에러의 기본 메시지를 추출
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                // List로 변환
                .collect(Collectors.toList());

        // Valid에서 나온 오류 리스트를 반환
        return new ResponseEntity<>(
                Response.error(errorList), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationHandler(ApplicationException e){
        log.error("Error occurs {}",e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e){
        log.error("Error occurs {}",e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
