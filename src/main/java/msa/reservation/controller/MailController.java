package msa.reservation.controller;


import lombok.RequiredArgsConstructor;
import msa.reservation.dto.request.MailCheckRequest;
import msa.reservation.dto.request.MailRequest;
import msa.reservation.dto.response.Response;
import msa.reservation.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@RestController
public class MailController {

    private final UserService userservice;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    // 인증 이메일 전송
    @PostMapping("/sendEmail")
    public HashMap<String, Object> mailSend(@RequestBody MailRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = userservice.sendMail(request.mail());
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public Response<?> mailCheck(@RequestBody MailCheckRequest request) {

        boolean isMatch = request.num() == number;

        if(isMatch){
            return Response.success(isMatch);
        }
        return Response.error(isMatch);
    }
}
