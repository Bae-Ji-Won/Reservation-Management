package msa.reservation.controller;

import lombok.RequiredArgsConstructor;
import msa.reservation.dto.UserDto;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.dto.request.UserLoginRequest;
import msa.reservation.dto.response.UserJoinResponse;
import msa.reservation.dto.response.Response;
import msa.reservation.dto.response.UserLoginResponse;
import msa.reservation.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        UserDto dto = userService.join(request);
        UserJoinResponse response = UserJoinResponse.from(dto);
        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.email(),request.password());
        return Response.success(new UserLoginResponse(token));
    }
}
