package msa.reservation.controller;

import lombok.RequiredArgsConstructor;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.dto.response.UserJoinResponse;
import msa.reservation.response.Response;
import msa.reservation.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){


        return Response.success()
    }

}
