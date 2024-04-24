package msa.reservation.controller;

import lombok.RequiredArgsConstructor;
import msa.reservation.dto.response.Response;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    @GetMapping
    public Response<String> create(Authentication authentication){
        return Response.success(authentication.getName());
    }

//    private final PostService postService;

//    @PostMapping
//    public Response<Void> create(@RequestBody PostCreateRequest, Authentication authentication){
//
//    }
}
