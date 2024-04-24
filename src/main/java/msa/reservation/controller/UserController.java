package msa.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import msa.reservation.dto.UserDto;
import msa.reservation.dto.request.MypageRequest;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.dto.request.UserLoginRequest;
import msa.reservation.dto.response.MypageResponse;
import msa.reservation.dto.response.UserJoinResponse;
import msa.reservation.dto.response.Response;
import msa.reservation.dto.response.UserLoginResponse;
import msa.reservation.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest request){
        UserDto dto = userService.join(request);
        UserJoinResponse response = UserJoinResponse.from(dto);
        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userService.login(request.email(),request.password());
        return Response.success(response);
    }

//    @DeleteMapping("/logout")
//    public Response<String> logout(Authentication authentication){
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        userService.logout(userDetails.getEmail());
//    }

    @GetMapping("/mypage")
    public Response<MypageResponse> mypage(Authentication authentication){
        MypageResponse response = userService.mypage(authentication);
        return Response.success(response);
    }


    @PutMapping("/mypage")
    public Response<String> mypageEdit(@Valid @RequestBody MypageRequest request, Authentication authentication){
        String result = userService.mypageEdit(request,authentication);
        return Response.success(result);
    }


}
