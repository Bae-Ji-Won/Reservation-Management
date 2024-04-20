package msa.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import msa.reservation.domain.entity.User;
import msa.reservation.dto.UserDto;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.exception.ApplicationException;
import msa.reservation.exception.ErrorCode;
import msa.reservation.repository.UserRepository;
import msa.reservation.util.JwtTokenUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    // TODO:"아래 키는 나중에 숨겨 놓을 것"

    private String encodeSecretKey = "password";
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    // 회원가입
    public UserDto join(UserJoinRequest request){


        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encodeSecretKey);
        String encodeUserName = encryptor.encrypt(request.userName());
        String encodeEmail = request.email()+encodeSecretKey;
        String encodeAddress =  encryptor.encrypt(request.address());
        String encodeCall = encryptor.encrypt(request.callNumber());

        // 이메일로 로그인
        userRepository.findByEmail(encodeEmail).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        });

        User user = userRepository.save(User.of(
                encodeUserName,
                encoder.encode(request.password()),
                encodeEmail,
                encodeAddress,
                encodeCall
        ));

        return UserDto.from(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email+encodeSecretKey).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 체크
        if(!encoder.matches(password,user.getPassword())){
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        
        String token = JwtTokenUtils.generateToken(user.getEmail(),secretKey,expiredTimeMs);

        return token;
    }


    // 로그인
//    public String login(UserLoginRequest request){
//
//    }
}
