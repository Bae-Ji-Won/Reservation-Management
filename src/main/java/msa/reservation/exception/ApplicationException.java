package msa.reservation.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public ApplicationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = null;
    }

    public ApplicationException(ErrorCode errorCode,String message){
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (message == null){
            return errorCode.getMessage();
        }

        return String.format("%s. %s",errorCode.getMessage(), message);
    }
}
