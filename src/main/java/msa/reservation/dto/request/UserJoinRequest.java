package msa.reservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
public class UserJoinRequest{

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,4}$" , message = "한글로만 구성된 2~4자리여야 합니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Length(min=8, max=50)
    private String password;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{11}$" , message = "숫자로만 입력해주세요")
    private String callNumber;
}
