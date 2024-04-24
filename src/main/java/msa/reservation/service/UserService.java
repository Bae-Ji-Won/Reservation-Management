package msa.reservation.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import msa.reservation.domain.entity.RefreshToken;
import msa.reservation.domain.entity.User;
import msa.reservation.dto.UserDto;
import msa.reservation.dto.request.MypageRequest;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.dto.response.MypageResponse;
import msa.reservation.dto.response.UserLoginResponse;
import msa.reservation.exception.ApplicationException;
import msa.reservation.exception.ErrorCode;
import msa.reservation.repository.UserRepository;
import msa.reservation.util.JwtTokenUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;


//    @Value("${spring.encodeEmailSecretKey}")
//    private String emailKey = "pw";

    @Value("${spring.encodeSecretKey}")
    private String encodeSecretKey;


    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Value("${jwt.token.refresh-expired-time-ms}")
    private Long refreshExpiredTimeMs;

    // -------------- email
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.senderEmail-address}")
    private String senderEmail;
    private static int number;




    // 회원가입
    public UserDto join(UserJoinRequest request){

        BasicTextEncryptor encryptor = new BasicTextEncryptor();

        encryptor.setPassword(encodeSecretKey);
        String encodeUserName = encryptor.encrypt(request.getUserName());
        String encodeEmail = request.getEmail();
//        String encodeEmail = request.getEmail()+emailKey;
        String encodeAddress =  encryptor.encrypt(request.getAddress());
        String encodeCall = encryptor.encrypt(request.getCallNumber());

        // 이메일로 로그인
        userRepository.findByEmail(encodeEmail).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_EMAIL);
        });

        User user = userRepository.save(User.of(
                encodeUserName,
                encoder.encode(request.getPassword()),
                encodeEmail,
                encodeAddress,
                encodeCall
        ));

        return UserDto.from(user);
    }

    public UserLoginResponse login(String email, String password) {
//        User user = userRepository.findByEmail(email+emailKey).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));


        // 비밀번호 체크
        if(!encoder.matches(password,user.getPassword())){
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String accessToken = JwtTokenUtils.generateToken("access",user.getEmail(),secretKey,expiredTimeMs);
        String refreshToken = JwtTokenUtils.generateToken("refresh",user.getEmail(),secretKey,refreshExpiredTimeMs);

        // RefreshToken을 DB에 저장
        // TODO: Redis로 연동하기
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.builder()
                .memberId(user.getId())
                .value(refreshToken)
                .build();

        UserLoginResponse response = new UserLoginResponse(accessToken, refreshToken);

        return response;
    }

    public void logout(String email) {
//        User user = userRepository.findByEmail(email+emailKey).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

    }

    // ---------------- email

    // 랜덤으로 숫자 생성
    public void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail) {

        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "아래의 인증번호를 입력해주세요" + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }

    // --------------- mypage

    public MypageResponse mypage(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        BasicTextEncryptor encryptor = new BasicTextEncryptor();

        encryptor.setPassword(encodeSecretKey);

//        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        UserDto dto = UserDto.from(user);

        // email 복호화
//        String encodeEmail = dto.email();
//        StringBuilder st= new StringBuilder(encodeEmail);
//        st.delete(encodeEmail.length() - 2, encodeEmail.length());
//        String decodeEmail = st.toString();


        // 유저 정보 복호화
        MypageResponse response = MypageResponse.of(
                dto.email(),
                encryptor.decrypt(dto.userName()),
                encryptor.decrypt(dto.address()),
                encryptor.decrypt(dto.callNumber())
        );

        return response;
    }

    public String mypageEdit(MypageRequest request,Authentication authentication){
        User user = (User) authentication.getPrincipal();
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 수정시
        if(!encoder.matches(request.getPassword(),user.getPassword())){
            // TODO: 비밀번호 변경시 모든 곳에서 로그아웃 구현
        }


        user.update(request);

        return "유저 정보가 수정되었습니다";
    }



}
