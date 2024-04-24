package msa.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MypageRequest{

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Length(min=8, max=50)
    private String password;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{11}$" , message = "숫자로만 입력해주세요")
    private String callNumber;
}
